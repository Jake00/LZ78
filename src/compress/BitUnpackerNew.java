package compress;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class BitUnpackerNew {
	
	IOHandler file;
	BufferedInputStream in = new BufferedInputStream(System.in);
	DataInputStream din = new DataInputStream(in);

	public BitUnpackerNew() throws IOException{
		unpack();
	}
	public BitUnpackerNew(String File) throws IOException{

		in = new BufferedInputStream(new FileInputStream(File));
		din = new DataInputStream(in);
		unpack();
	}


	public void unpack() throws IOException {
		byte[] inputBytes = new byte[7];
		int bitIndex = 8, byteIndex = 0;
		
		int bytesRead = 0;
		while ((bytesRead = in.read(inputBytes)) > 0) {
			bitIndex -= 5;
			int flag;
			
			if (bitIndex < 0) {
				int l1 = bitIndex + 5;
				int l2 = Math.abs(bitIndex);
				int bits1 = getBits(inputBytes[byteIndex], l1, 0);
				int bits2 = getBits(inputBytes[++byteIndex], l2, 8 - l2);
				flag = concatBits(bits1, bits2, l2);
				bitIndex += 8;
			} else {
				flag = getBits(inputBytes[byteIndex], 5, bitIndex);
			}
			
			bitIndex -= flag;
			int phraseNum;
			
			if (bitIndex < 0) {
				int l1 = bitIndex + flag;
				int bits1 = getBits(inputBytes[byteIndex], l1, 0);
				int bytesSpanned = bitIndex / 8;
				for (int i = 0; i < bytesSpanned ; i++) {
					int bits2 = getBits(inputBytes[++byteIndex], 8, 0);
					bits1 = concatBits(bits1, bits2, 8);
				}
				int length2 = Math.abs(bitIndex % 8);
				int bits2 = getBits(inputBytes[++byteIndex], length2, 8 - length2);
				phraseNum = concatBits(bits1, bits2, length2);
			} else {
				phraseNum = getBits(inputBytes[byteIndex], flag, bitIndex);
			}
			
			bitIndex -= 8;
//			int character
		}
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
	 * Concatenates two sets of bits together.
	 * @param num1 The first set of bits.
	 * @param num2 The second set of bits.
	 * @param offset The length of the second set of bits.
	 * @return The result of the concatenation.
	 */
	private int concatBits(int num1, int num2, int offset) {
		return (num1 << offset) & num2;
	}
	
	private int concatBits(int[] nums)
}
