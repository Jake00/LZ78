package compress;

/**
 * @author Jake Bellamy 1130587 jrb46
 * @author Michael Coleman 1144239 mjc62
 */
public class Encoder {
	
	Trie dictionary;
	TrieNode dict;
	IOHandler io;

	public Encoder() {
		dictionary = new Trie();
		io = new IOHandler();
	}
	
	public Encoder(String fileName) {
		dictionary = new Trie();
		io = new IOHandler(fileName);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Encoder e;
		if (args.length == 0) {
			e = new Encoder();
			System.out.println("Encoding from standard input.\n" +
					"Please enter the text you wish to encode " +
					"followed by an empty line.");
		} else {
			e = new Encoder(args[0]);
			System.out.println("Encoding from file " + args[0] + "...");
		}
		
		e.encode();
	}
	
	public void encode() {
		/**
		 * Current prefix (P): the prefix currently being processed in the 
		 * encoding algorithm.
		 * Current character (C): a character determined in the encoding algorithm. 
		 * Generally this is the character preceded by the current prefix.
		 * 
		 * The encoding algorithm
		 * 1. At the start, the dictionary and P are empty; 
		 * 2. Assign C next character in the charstream;
		 * 3. Is the string P+C present in the dictionary?
		 * 		a. If YES assign P = P+C (extend P with C);
		 * 		b. If NO,
		 * 			i. Output these two objects to the codestream:
		 * 				- The code word corresponding to P (if P is empty, output a zero);
		 * 				- C, in the same form as input from the charstream;
		 * 			ii. Add the string P+C to the dictionary;
		 * 			iii. Assign P to be empty;
		 * 		c. Are there more characters in the charstream?
		 * 			- If YES return to step 2;
		 * 			- If NO,
		 * 				i. If P is not empty, output the code word corresponding to P;
		 * 				ii. END
		 */
		//Position of the tape head
		int position = 0;
		//Current prefix is length zero
		boolean firstByte = true;
		//Byte buffer
		byte[] in = new byte[10];
		//Number of bytes read into the buffer
		int bytesRead;
		
		//Read in more input into the buffer then loop through the buffer
		while ((bytesRead = io.readBytes(in)) > 0) {
			for (int i = 0; i < bytesRead; i++) {
				//Next character in the charstream
				byte c = in[i];
				
				/**
				 * Whether we have any characters encoded before this one 
				 * will determine whether we are searching and possibly 
				 * adding to the dictionary on the top level, or from one 
				 * of the lower nodes.
				 */
				if (firstByte) {
					dict = dictionary.getNode(c);
					
					/**
					* Check if we have an encoding in our dictionary for 
					* this new character.
					*/ 
					if (dict == null) {
						/**
						* If not, store C and its index. Output C with
						* a zero to say we have not seen this character
						* at all before
						*/
						position++;
						dictionary.addNode(c, position);
						io.writeBytes(toBytes(0, c));
						continue;
					} else {
						/**
						 * If so, change the flag and continue iteration 
						 * as our node is set now.
						 */
						firstByte = false;
						continue;
					}
					
				} else { 
					/** We have characters encoded before this one */
					TrieNode next = dict.getNode(c);
					
					/**
					 * Check if we have an encoding in our dictionary for 
					 * this new character. 
					 */
					if (next == null) {
						/**
						 * If not, store C and its index. Output C with 
						 * the index of its parent (the last matched 
						 * encoding in the dictionary)
						 */
						position++;
						dict.addNode(c, position);
						io.writeBytes(toBytes(dict.position, c));
						firstByte = true;
						dict = null;
						continue;
					} else {
						/**
						 * If so, set our reference node to the new char.
						 */
						dict = next;
						continue;
					}
				}
				
			}
		}
		/**
		 * If we have a current matched character after the char stream 
		 * has emptied then we need to output it along with the NUL marker.
		 */
		if (dict != null) {
			io.writeBytes(toBytes(dict.position, (byte) 0));
		}
		io.closeFileStreams();
	}

	private byte[] toBytes(int i, byte b) {
		return new byte[] {
			(byte) ((i >> 24) & 0xFF),
			(byte) ((i >> 16) & 0xFF),
			(byte) ((i >> 8) & 0xFF),
			(byte) (i & 0xFF),
			b
		};
	}
}
