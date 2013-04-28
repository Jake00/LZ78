package compress;

import java.io.*;

public class BitUnpacker {
	IOHandler io;
	int bytesRead;

	private BitUnpacker() throws IOException{
		unpack();
	}
	private BitUnpacker(String File) throws IOException{
		io = new IOFileHandler(File);
		unpack();
	}
	public BitUnpacker(IOHandler io){
		this.io = io;
	}

	public static void main(String[] args) {
		try {
			if(args[0] == null){
				BitUnpacker bup = new BitUnpacker();
			}
			else{
				BitUnpacker bup = new BitUnpacker(args[0]);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * iterates through the byte array array of 7 elements containing the flag, phrasenumber and character, then shuffles out used bytes and inserts
	 * new bytes until the end of file is reached. 
	 * @throws IOException
	 */
	public void unpack(){
		try{
			byte[] inputBytes = new byte[7];
			int bitIndex = 8, byteIndex = 0;

			bytesRead = io.readBytes(inputBytes);
			while (bytesRead > 0) {
				bitIndex -= 5;
				Integer flag;

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
				Integer phraseNum;

				if (bitIndex < 0) {
					int l1 = bitIndex + flag;
					int bits1 = getBits(inputBytes[byteIndex], l1, 0);
					int bytesSpanned = Math.abs(bitIndex / 8);
					for (int i = 0; i < bytesSpanned ; i++) {
						int bits2 = getBits(inputBytes[++byteIndex], 8, 0);
						bits1 = concatBits(bits1, bits2, 8);
						bitIndex += 8;
					}
					int length2 = Math.abs(bitIndex % 8);
					int bits2 = getBits(inputBytes[++byteIndex], length2, 8 - length2);
					phraseNum = concatBits(bits1, bits2, length2);
					bitIndex += 8;
				} else {
					phraseNum = getBits(inputBytes[byteIndex], flag, bitIndex);
				}

				bitIndex -= 8;
				int character;

				if(bitIndex <= 0){
					int l1 = bitIndex + 8;
					int l2 = Math.abs(bitIndex);
					int bits1 = getBits(inputBytes[byteIndex], l1, 0);
					int bits2 = getBits(inputBytes[++byteIndex], l2, 8 - l2);
					character = concatBits(bits1, bits2, l2);
				}else{
					character = getBits(inputBytes[byteIndex], 8, bitIndex);
				}
				System.out.println(phraseNum.toString() + (char)character);
				bitIndex += 8;		
				inputBytes = shiftDown(inputBytes, byteIndex);
				byteIndex = 0;
			}
		}catch(IOException e){
			e.printStackTrace();
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
	return (num1 << offset) ^ num2;
}

/***
 * Shifts old bytes out of the array and fills the array with new bytes
 * @param array The array of bytes
 * @param num The number of bytes to shift out
 * @return The shifted byte array.
 * @throws IOException
 */
private byte[] shiftDown(byte[] array, int num) throws IOException{
	for(int i = num; i < array.length; i++){
		array[i-num] = array[i];
		array[i] = 0;
	}
	bytesRead -= num;
	byte[] b = new byte[1]; 
	for(int i = array.length - num; i < array.length; i++){
		if(io.readBytes(b) > 0){
			array[i] = b[0];
			bytesRead++;
		}
	}
	return array;
}
}
