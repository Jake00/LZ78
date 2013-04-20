package compress;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.*;

/**
 * @author Jake Bellamy 1130587 jrb46
 * @author Michael Coleman 1144239 mjc62
 */
public class Decoder {

	IOHandler io;
	ArrayList<String> dictionary = new ArrayList<String>();

	/**
	 * Construct a new tuple decoder with input and output to and from 
	 * the console.
	 */
	public Decoder() {
		io = new IOConsoleHandler();
	}

	/**
	 * Construct a new tuple decoder with input and output pointing to
	 * a file.
	 * @param fileName The file to read tuples in from.
	 */
	public Decoder(String fileName) {
		io = new IOFileHandler(fileName);
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

		try {
			d.decode();
		} catch(NumberFormatException e){
			System.err.println("Not valid encoded message. Proper format is: \"...{int[],char}{int[],char}...\"");
		}
	}

	public void decode() {
//0A0n2a0 1t0e4A4B0a3n9
		int startindex = 0;
		int mindex = 0;
		byte[] buf = new byte[50];
		String message = new String();
		
		while (io.readBytes(buf) > 1) {
			try {
				String messagePart = new String(buf, "UTF8");
				message = message.concat(messagePart);
			} catch (UnsupportedEncodingException e) {
				System.err.println(e.getMessage());
			}
		}
		
		Pattern p = Pattern.compile("[^0-9]");
		Matcher m = p.matcher(message);

		while (m.find() == true) {
			mindex = m.end();
			String tupple = message.substring(startindex, mindex);
			startindex = mindex;
			if (tupple.length() > 1) {
				dictionary.add(tupple);
				output(tupple);
			}
		}
		io.closeAllStreams();
	}

	public void output(String tupple) throws NumberFormatException {
		int previndex = Integer.parseInt(tupple.substring(0, tupple.length() -1)); //gets the last index out of the tupple.
		if(previndex > 0)
			output(dictionary.get(previndex -1));
//		System.out.print(tupple.substring(tupple.length() -1));
		io.writeBytes(tupple.substring(tupple.length() - 1).getBytes());
		
		}

	}
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

