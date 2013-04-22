package compress;

import java.io.*;


public class BitUnpacker {

	IOHandler file;
	BufferedInputStream in = new BufferedInputStream(System.in);
	DataInputStream din = new DataInputStream(in);

	public BitUnpacker(){
	}
	public BitUnpacker(String File){

	}

	public void Unpack() throws IOException{

		byte[] inputbytes = new byte[7];

		int flagindex = 0;
		int indexindex;
		int charindex;

		inputbytes[0] = din.readByte();
		while(true){
			int flag;
			int index;
			char character;

			if(8 - (flagindex + 5) > 0){
				flag = GetFlag(flagindex, inputbytes);
				indexindex = flagindex + 5;
			}
			else{
				inputbytes[1] = din.readByte();
				flag = GetFlag(flagindex, inputbytes);
				inputbytes = StepUp(inputbytes);
				indexindex = flagindex - 3; 
			}

			int counter = flag;
			while (counter > 0){
				
			}





		}
	}

	public byte[] StepUp(byte[] array){
		for(int i = 0; i < array.length; i++){
			array[i] = array[i +1];
		}
		return array;
	}

	/**
	 * 
	 * @param startloc = the index into the beginning of the flag
	 * @param flagbytes = the bytes containing the flag data. may only be one byte.
	 * @return the index into the byte of the first bit significant to the value of the index
	 */
	public int GetFlag(int startloc, byte[] flagbytes){
		int flag = 0;
		byte firstbyte = flagbytes[0];
		byte secondbyte = flagbytes[1];
		firstbyte <<= startloc;
		secondbyte >>>= 8 - startloc; 
		flag = firstbyte | secondbyte;
		flag >>>= 3; 
		return flag;
	}

	public int GetIndex(int startloc, int flag, byte[] indexbytes){
		int index = 0;
		int duration = flag;
		byte firstbyte = indexbytes[0];
		firstbyte <<= startloc;
		index = firstbyte >>> startloc;
		duration -= 8 - startloc;
		int counter = 1;

		while (duration > 0){

			byte nextbyte = indexbytes[counter]; 
			if(duration >= 8)
				index <<= 8;
			else{
				index <<= duration;
				nextbyte >>>= 8 - duration; }
			index = index | nextbyte; 
			counter ++;
		}
		return index;
	}

	public char GetCharacter(int startloc, byte[] charbytes){
		char character;
		byte firstbyte = charbytes[0];
		byte secondbyte = charbytes[1];
		firstbyte <<= startloc;
		secondbyte >>>= 8 - startloc;
		character = (char) (firstbyte | secondbyte);
		return character;
	}




	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
