package compress;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * @author Jake Bellamy 1130587 jrb46
 * @author Michael Coleman 1144239 mjc62
 */
public class BitPacker {

	IOHandler io;

	/**
	 * Construct a new BitPacker reading from standard in.
	 */
	public BitPacker() {
		io = new IOConsoleHandler();
	}

	/**
	 * Construct a new BitPacker reading from a file.
	 * @param filename The file name to read from.
	 */
	public BitPacker(String filename) {
		io = new IOFileHandler(filename);
	}

	/**
	 * Construct a new BitPacker with a given IO Handler.
	 * @param io The IO Handler to use with this bit packer.
	 */
	public BitPacker(IOHandler io) {
		this.io = io;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BitPacker bp;
		if (args.length > 0) {
			bp = new BitPacker(args[0]);
			System.out.println("Bitpacking tuples from file " + 
					args[0] + "...");
		} else {
			bp = new BitPacker();
			System.out.println("Packing from standard input.\n Please " +
					"enter the tuples you wish to pack followed by an " +
					"empty line.");
		}
		bp.pack();
	}

	/**
	 * Reads tuples from the IO Handler and packs the tuples so they use
	 * the minimum amount of bits needed.
	 */
	public void pack() {
		int indexLength = 5;
		byte newLineChar = System.getProperty("line.separator").getBytes()[0];

		int startindex = 0, mindex = 0, byteIndex = 0, bitIndex = 8;
		int bufLength = 100000;
		byte[] inbuf = new byte[1024];
		byte[] buf = new byte[bufLength + 1];
		int bytesRead;
		int bytesToRead = inbuf.length;
		int readOffset = 0;
		/**
		 * Loop through each tuple that we find, calculate lengths and
		 * indices.
		 */
		while ((bytesRead = io.readBytes(inbuf, readOffset, bytesToRead)) > 0) {
			for (int i = 0; i < bytesRead; i++) {

				if (inbuf[i] == newLineChar) {
					mindex = i;
				} else {
					continue;
				}
				
				if (i+1 < inbuf.length && inbuf[i+1] == newLineChar) {
					mindex++;
					i++;
				}
				int tupleLength = mindex - startindex;
				int phraseNum;
				if (mindex - 2 == 0) {
					phraseNum = Integer.parseInt(new String
							(new byte[] { inbuf[startindex] },
									Charset.forName("UTF-8")));
				} else {
					phraseNum = Integer.parseInt(new String
							(Arrays.copyOfRange(inbuf, startindex, mindex - 1), 
									Charset.forName("UTF-8")));
				}
				int character = inbuf[mindex - 1];
				int phraseLength = getBitLength(phraseNum);

				startindex = mindex;
				/**
				 * With each tuple that we find we are outputting 3 things:
				 * The number of bits needed to represent the phrase number,
				 * The phrase number itself,
				 * And the character.
				 */
				for (int j = 0; j < 3; j++) {
					int[] vars = setVars(j, indexLength, phraseLength, 
							phraseNum, character);

					/**
					 * Set the next offset for the placement of the next bits 
					 * that we are inserting into the byte. If the offset is
					 * negative that indicates that there is not enough space
					 * in this byte to store these bits completely so we must
					 * split it up and store the overflow into the next byte.
					 */
					bitIndex -= vars[0];
					if (bitIndex < 0) {
						//Calculate indices and lengths for correct placement.
						int topbits = bitIndex + vars[0];
						int off = Math.abs(bitIndex);
						/**
						 * Store what we can fit into this byte, and the rest
						 * in the next byte.
						 */
						buf[byteIndex]= setBits
								(buf[byteIndex], vars[1], off, topbits);

						int bytesSpanned = Math.abs(bitIndex / 8);
						for (;bytesSpanned > 0; bytesSpanned--) {
							int shiftDistance = ((bytesSpanned - 1) * 8) + Math.abs((bitIndex % 8));
							buf[++byteIndex] = setBits(buf[byteIndex], vars[1], shiftDistance, 8);
							bitIndex += 8;
							if (byteIndex == bufLength) {
								buf = outputBuffer(buf, bufLength, byteIndex);
								byteIndex = 0;
							}
						}

						off = Math.abs(bitIndex);
						buf[++byteIndex] = setBits
								(buf[byteIndex], vars[1], 8 - off);

						//Fix up the bit index so it references this new byte
						bitIndex += 8;
					} else {
						/**
						 * The bits we are storing fits into the space left
						 * in this byte so put all of them in.
						 */
						buf[byteIndex] = setBits
								(buf[byteIndex], vars[1], bitIndex);
					}

					/**
					 * Check if we have reached the length of our buffer.
					 * Output the entire buffer except the last byte as there 
					 * may be more space left in it to insert bits into.
					 * Swap the last byte to the first position of our new
					 * buffer and fix up the current byte index.
					 */
					if (byteIndex == bufLength) {
						buf = outputBuffer(buf, bufLength, byteIndex);
						byteIndex = 0;
					}
				}
			}
			readOffset = inbuf.length - mindex;
			
			for (int i = 0; i < readOffset; i++) {
				inbuf[i] = inbuf[mindex+i];
			}
			
			bytesToRead = mindex;
			startindex = 0;
		}
		/**
		 * There is no more input so write out the data we collected and
		 * close the streams.
		 */
		io.writeBytes(buf, byteIndex + 1);
		io.closeAllStreams();

	}

	private byte[] outputBuffer(byte[] buf, int bufLength, int byteIndex) {
		io.writeBytes(buf, bufLength);
		byte swap = buf[byteIndex];
		buf = new byte[bufLength + 1];
		buf[0] = swap;
		return buf;
	}

	/**
	 * Gets some bits out of a byte. For example to extract the x bits
	 * from a byte given as {@code 000x xx00} then call this method with 
	 * {@code length = 3} and {@code offset = 2}.
	 * @param b The byte we are extracting bits from.
	 * @param length The number of bits to extract.
	 * @param offset The starting position in the byte to extract from.
	 * @return An integer with the extracted bits.
	 */
	private int getBits(byte b, int length, int offset) {
		/**
		 * Build up a mask full of 1-bits for ANDing with the byte that 
		 * we want the bits out of.
		 */
		int mask = 0;
		for (int i = 0; i < length; i++) {
			mask = (mask << 1) + 1;
		}
		return (b >> offset) & mask;
	}

	/**
	 * Sets bits in a byte.
	 * @param b The byte in which to store bytes into.
	 * @param bits The bits to store.
	 * @param offset The position in which to store the bits.
	 * @return The same byte except with the given bits set in it.
	 */
	private byte setBits(byte b, int bits, int offset) {
		return (byte) (b | (bits << offset));
	}

	/**
	 * Sets some partial bits in a byte.
	 * @param b The byte to store bits into.
	 * @param bits The bits to insert in.
	 * @param offset The position in the byte in which to store the bits.
	 * @param length How many bits should be stored in the byte.
	 * @return The same byte but with the bits set.
	 */
	private byte setBits(byte b, int bits, int offset, int length) {
		int mask = 0;
		for (int i = 0; i < length; i++) {
			mask = (mask << 1) + 1;
		}
		return (byte) (b | ((bits >> offset) & mask));
	}

	/**
	 * Gets the minimum number of bits needed to represent a given number.
	 * @param num The number to check.
	 * @return The number of significant bits in the number.
	 */
	private int getBitLength(int num) {
		return (32 - Integer.numberOfLeadingZeros(num));
	}

	/**
	 * Sets up variables passed in by the packing method. This is a simple
	 * convenience method so the variables may be used in a for loop.
	 */
	private int[] setVars(int i, int indexLength, int phraseLength, 
			int phraseNum, int character) {
		int[] vars = new int[2];
		switch (i) {
		case 0:
			vars[0] = indexLength;
			vars[1] = phraseLength;
			return vars;
		case 1:
			vars[0] = phraseLength;
			vars[1] = phraseNum;
			return vars;
		case 2:
			vars[0] = 8;
			vars[1] = character;
			return vars;
		default:
			return vars;
		}
	}
}
