package compress;

/**
 * @author Jake Bellamy 1130587 jrb46
 * @author Michael Coleman 1144239 mjc62
 */
public class Decoder {

	Trie dictionary;
	IOHandler file;
	
	public Decoder() {
		file = new IOHandler();
	}
	
	public Decoder(String fileName) {
		file = new IOHandler(fileName);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Decoder d;
		if (args.length == 0) {
			d = new Decoder();
			System.out.println("Decoding from standard input.\n Please " +
					"enter the tuples you wish to decode in the format " +
					"(number, character) followed by an empty line.");
		} else {
			d = new Decoder(args[0]);
			System.out.println("Decoding from file " + args[0] + "...");
		}
		
		d.decode();
	}
	
	public void decode() {
		/**
		 * Current character (C): a character determined in the encoding algorithm.
		 * Generally this is the character preceded by the current prefix.
		 * Current code word (W): the code word currently processed in the decoding 
		 * algorithm. It is signified by W, and the string which it denotes by string.
		 * 
		 * 1. At the start the dictionary is empty; 
		 * 2. Assign W = next code word in the codestream; 
		 * 3. Assign C = the character following it; 
		 * 4. Output the string.W to the codestream (this can be an empty string), 
		 *    and then output C; 
		 * 5. add the string.W+C to the dictionary; 
		 * 6. are there more code words in the codestream? 
		 * 		- If YES go back to step 2
		 * 		- If NO, END.
		 */
		
	}

}
