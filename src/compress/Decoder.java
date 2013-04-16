package compress;

import java.io.IOException;
import java.util.regex.*;

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
		
		//d.decode();
	}
	
	public void decode() throws IOException {
		int startindex = 0;
		int mindex;
		String message = file.stdIn.readLine();
		Pattern p = Pattern.compile("a-zA-Z");
		Matcher m = p.matcher(message);
		
		while(m.find() == true){
			mindex = m.start();
			int lookup = (Integer.parseInt(message.substring(startindex, mindex)));
			//if(lookup != 0)
				//dictionary.
		}
		
	}

}
