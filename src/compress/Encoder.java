package compress;

/**
 * @author Jake Bellamy 1130587 jrb46
 * @author Michael Coleman 1144239 mjc62
 */
public class Encoder {
	Trie dictionary;
	IOHandler io;

	public Encoder() {
		dictionary = new Trie();
		io = new IOConsoleHandler();
	}
	
	public Encoder(int maxNumBits) {
		dictionary = new Trie(maxNumBits);
		io = new IOConsoleHandler();
	}
	
	public Encoder(String filename) {
		dictionary = new Trie();
		io = new IOFileHandler(filename);
	}
	
	public Encoder(String filename, int maxNumBits) {
		dictionary = new Trie(maxNumBits);
		io = new IOFileHandler(filename);
	}
	
	public Encoder(IOHandler io) {
		dictionary = new Trie();
		this.io = io;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Encoder e;
		
		if (args.length == 0) {
			e = new Encoder();
			System.out.println("Encoding from standard input.\n Please " +
					"enter the text you wish to encode followed by an " +
					"empty line.");
		} else if (args.length == 1) {
			e = new Encoder(args[0]);
			System.out.println("Encoding from file " + args[0] + "...");
		} else {
			int maxbits = 20;
			String filename = null;
			for (int i = 0; i < args.length; i++) {
				if (args[i].equalsIgnoreCase("-b")) 
					maxbits = Integer.parseInt(args[++i]);
				else
					filename = args[i];
			}
			if (maxbits < 8) {
				System.out.println("Maximum bits for encoding cannot be" +
						"less than 8. Defaulting to 8...");
				maxbits = 8;
			}
			e = new Encoder(filename, maxbits);
			System.out.println("Encoding from file " + filename + "...");
		}
		
		e.encode();
	}
	
	public void encode() {

		//A node representing a character in our dictionary
		TrieNode node = null;
		//The number corresponding to each unique phrase in our dictionary
		int phraseNum = 0;
		//Input byte buffer
		byte[] in = new byte[100];
		//Number of bytes read into the buffer
		int bytesRead;
		//Bytes in / bytes out
		float compressionRatio = 1.0f;
		int bytesOut;
		
		io.writeBytes(new byte[] { 52 });
		
		//Read more input into the buffer then loop through the buffer
		while ((bytesRead = io.readBytes(in)) > 1) {
			bytesOut = 0;
			for (int i = 0; i < bytesRead; i++) {
				
				//Next character in the charstream
				byte c = in[i];
				//Get the next node in our dictionary if it exists
				node = dictionary.getNode(c);
				
				/**
				 * If there is no entry in our dictionary with this string
				 * of bytes then add it and write out the tuple consisting
				 * of the index of the longest matching prefix and the the
				 * character that is not yet encoded.
				 */
				if (node == null) {
					phraseNum++;
					bytesOut++;
					dictionary.addNode(c, phraseNum);
					io.writeTuples(c, dictionary.getPosition());
					dictionary.setFirst();
				}
			}
			//Calculate the new ratio to compare against the old.
			float newRatio = (float) bytesRead / bytesOut;
			//If our ratio has dropped by 20 percent then throw out the
			//old dictionary and create a new one.
			if (newRatio / compressionRatio < 0.8) {
				dictionary = new Trie(dictionary.getMaxBits());
			}
			compressionRatio = newRatio;
		}
		/**
		 * If we have a current matched character after the char stream 
		 * has emptied then we need to output it along with the NUL marker.
		 */
		if (node != null) {
			io.writeTuples((byte) 0, node.position);
		}
		io.closeAllStreams();
	}

}
