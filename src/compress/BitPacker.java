package compress;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jake Bellamy 1130587 jrb46
 * @author Michael Coleman 1144239 mjc62
 */
public class BitPacker {
	
	IOHandler io;
	
	public BitPacker() {
		io = new IOConsoleHandler();
	}
	
	public BitPacker(String filename) {
		io = new IOFileHandler(filename);
	}
	
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
			System.out.println("Encoding from file " + args[0] + "...");
		} else {
			bp = new BitPacker();
			System.out.println("Packing from standard input.\n Please " +
					"enter the tuples you wish to pack followed by an " +
					"empty line.");
		}
		bp.pack();
	}
	
	public void pack() {
		byte[] buf = new byte[1];
		io.readBytes(buf);
		int indexLength = Integer.parseInt
				(new String(buf, Charset.forName("UTF-8")));
		
		String message = io.readString();
		Pattern p = Pattern.compile("[^0-9]");
		Matcher m = p.matcher(message);
		int startindex = 0, mindex = 0, byteIndex = 0, bitIndex = 6;
		int bufLength = 50;
		buf = new byte[bufLength + 1];
		setBits(buf[0], encodeIndexLength(indexLength), bitIndex);
		
		while (m.find()) {
			mindex = m.end();
			int phraseNum = Integer.parseInt(message.substring(startindex, mindex - 1));
			int character = message.charAt(mindex - 1);
			int phraseLength = getBitLength(phraseNum);
			startindex = mindex;

			for (int i = 0; i < 3; i++) {
				int[] vars = setVars(i, indexLength, phraseLength, phraseNum, character);
				
				bitIndex -= vars[0];
				if (bitIndex < 0) {
					int topbits = bitIndex + vars[0];
					int off = Math.abs(bitIndex);
					buf[byteIndex]= setBits(buf[byteIndex], vars[1], off, topbits);
					buf[++byteIndex] = setBits(buf[byteIndex], vars[1], 8 - off);
					bitIndex += 8;
				} else {
					buf[byteIndex] = setBits(buf[byteIndex], vars[1], bitIndex);
				}
				
				if (byteIndex == bufLength) {
					io.writeBytes(buf, bufLength);
					byte swap = buf[byteIndex];
					buf = new byte[bufLength + 1];
					buf[0] = swap;
					byteIndex = 0;
				}
			}

		}
		io.writeBytes(buf, byteIndex + 1);
		io.closeAllStreams();
		
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
		 * Build up a mask full of 1-bits for ANDing with the byte that we
		 * want the bits out of.
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

	private int encodeIndexLength(int indexBitLength) {
		switch (indexBitLength) {
		case 4:
			return 0b00;
		case 5:
			return 0b01;
		case 6:
			return 0b10;
		default:
			return 0b11;
		}
	}
	
	private int[] setVars(int i, int indexLength, int phraseLength, int phraseNum, int character) {
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
