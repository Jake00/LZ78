package compress;

import java.io.*;


public class BitUnpackerOld {

	IOHandler file;
	BufferedInputStream in = new BufferedInputStream(System.in);
	DataInputStream din = new DataInputStream(in);

	public BitUnpackerOld() throws IOException{
		Unpack();
	}
	public BitUnpackerOld(String File) throws IOException{

		in = new BufferedInputStream(new FileInputStream(File));
		din = new DataInputStream(in);
		Unpack();
	}

	public void Unpack() throws IOException{

		byte[] inputbytes = new byte[7];
		int flagindex = 0;
		int indexindex;
		int charindex;

		inputbytes[0] = din.readByte();
		while(true){
			Integer flag = 0;
			Integer index = 0;
			char character;
			////
			if(flagindex + 5 > 8)
//				inputbytes[1]
			flag = GetFlag(flagindex, inputbytes); //get flag from flag index
			//
			indexindex = (flagindex + 5) % 8; //get index of index from flag index
			if((flagindex + 5) / 8 > 0){ //delete old byte if flag wraps 2 bytes
				inputbytes = StepUp(inputbytes);
				inputbytes[0] = din.readByte();
			}
			///
			for(int i = 0; i < flag + indexindex/8; i ++){ //read in the number of bytes to get entire index
				inputbytes[1 + i] = din.readByte();
			}
			index = GetIndex(indexindex, flag, inputbytes); //get the index from the bytes
			///
			for(int i = (indexindex + flag) / 8; i > 0; i--){ //delete all bytes not needed.
				inputbytes = StepUp(inputbytes);	
			}
			charindex = (indexindex + flag) % 8; //get the index of the char bits from the index index + length
			if(charindex + 8 > 8 && inputbytes[1] == 0){
				inputbytes[1] = din.readByte();
			}
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
		secondbyte = (byte) ((secondbyte & 0xff) >> 8 - startloc); 
		flag = firstbyte ^ secondbyte;
		flag >>= 3; 
		return flag;
	}

	public int GetIndex(int startloc, int flag, byte[] indexbytes){
		int index = 0;
		int duration = flag;
		byte firstbyte = indexbytes[0];
		firstbyte <<= startloc;
		int tempindex = (firstbyte & 0xff) >>> (8 - flag);
		duration -= 8 - startloc;
		int counter = 1;

		while (duration > 0){

			byte nextbyte = indexbytes[counter]; 
			if(duration >= 8){
				tempindex <<= 8;
				duration -= 8;
			}
			else{
				//tempindex <<= duration;
				nextbyte = (byte) ((nextbyte & 0xff) >>> 8 - duration); 
				duration = 0;
			}
			tempindex = tempindex | nextbyte; 
			counter ++;
		}
		index = tempindex;
		return index;
	}

	public char GetCharacter(int startloc, byte[] charbytes){
		char character;
		byte firstbyte = charbytes[0];
		byte secondbyte = charbytes[1];
		firstbyte <<= startloc;
		secondbyte = (byte) ((secondbyte & 0xff)  >>> 8 - startloc);
		character = (char) (firstbyte | secondbyte);
		return character;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if(args[0] == null){
				BitUnpackerOld bup = new BitUnpackerOld();
			}
			else{
				BitUnpackerOld bup = new BitUnpackerOld(args[0]);
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

