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
	 * Construct a new tuple decoder with a given IO handler.
	 * @param io The IO handler to use.
	 */
	public Decoder(IOHandler io) {
		this.io = io;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Decoder d;
		if (args.length == 0) {
			d = new Decoder();
			System.out.println("Decoding from standard input.\n" +
					"Please enter the tuples you wish to decode" +
					" in the format: Number Character (with" +
					" no space) followed by an empty line.");
		} else {
			d = new Decoder(args[0]);
			System.out.println
			("Decoding from file " + args[0] + "...");
		}
		
		d.decode();
	}

	/**
	 * Decodes tuples back into the original data.
	 */
	public void decode() {
		String newLineChar = System.getProperty("line.separator");
		//Indices for splitting up the phrase numbers from characters.
		int startindex = 0, mindex = 0;
		//Get the data to be decoded.
		String message = io.readString();
		//Pattern and Matcher used to actually split the data.
		Pattern p = Pattern.compile(newLineChar); //[^0-9]
		Matcher m = p.matcher(message);

		/**
		 * Loop through each match, add it to our array and output it.
		 * Each match from the matcher returns an index in our string 
		 * so we must split it up ourselves.
		 */
		while (m.find()) {
			mindex = m.end();
			String tupple = message.substring(startindex, mindex - 1);
			startindex = mindex;

			if (tupple.length() > 1) {
				dictionary.add(tupple);
				output(tupple);
			}
		}
		io.closeAllStreams();
	}

	/**
	 * Outputs the character from the tuple passed in. Checks the index
	 * and outputs the next characters if the tuple's index points to
	 * another element in our dictionary.
	 * @param tupple A tuple in the format "IndexCharacter".
	 * @throws NumberFormatException If parsing the index fails.
	 */
	private void output(String tupple) throws NumberFormatException {
		int tupleLength = tupple.length() - 1;
		//Gets the index out of the tuple.
		int previndex = Integer.parseInt 
								(tupple.substring(0, tupleLength)); 
		
		if (previndex > 0) {
			output(dictionary.get(previndex -1));
		}
		
		byte[] character = tupple.substring(tupleLength).getBytes();
		if (character[0] != 0) {
			io.writeBytes(character);
		}

	}

}