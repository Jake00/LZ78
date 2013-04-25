package compress;

import java.io.*;


public class BitUnpacker {

	IOHandler file;
	BufferedInputStream in = new BufferedInputStream(System.in);
	DataInputStream din = new DataInputStream(in);

	public BitUnpacker() throws IOException{
		Unpack();
	}
	public BitUnpacker(String File){

	}

	public void Unpack() throws IOException{

		byte[] inputbytes = new byte[7];
		int flagindex = 0;
		int indexindex;
		int charindex;

		inputbytes[0] = din.readByte();
		inputbytes[1] = din.readByte();
		while(inputbytes[0] != 0){
			Integer flag;
			Integer index;
			char character;
			////
			flag = GetFlag(flagindex, inputbytes); //get flag from flag index
			//
			indexindex = (flagindex + 5) % 8; //get index of index from flag index
			if((flagindex + 5) / 8 > 0) //delete old byte if flag wraps 2 bytes
				inputbytes = StepUp(inputbytes);
			///
			for(int i = 0; i < flag/8; i ++){ //read in the number of bytes to get entire index
				inputbytes[1 + i] = din.readByte();
			}
			index = GetIndex(indexindex, flag, inputbytes); //get the index from the bytes
			///
			for(int i = (indexindex + flag) / 8; i > 0; i--){ //delete all bytes not needed.
				inputbytes = StepUp(inputbytes);	
			}
			charindex = (indexindex + flag) % 8; //get the index of the char bits from the index index + length
			character = GetCharacter(charindex, inputbytes);
			
			if((charindex + 8) / 8 > 0)
				inputbytes = StepUp(inputbytes);
			flagindex = (charindex + 8) % 8;
			
			System.out.println(flag.toString() + index.toString() + character); 

		}
	}

	public byte[] StepUp(byte[] array){
		for(int i = 0; i < array.length - 1; i++){
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
		try {
			BitUnpacker bup = new BitUnpacker();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
