package compress;

import java.io.*;
import java.util.Arrays;

/**
 * @author Jake Bellamy 1130587 jrb46
 * @author Michael Coleman 1144239 mjc62
 */
public class IOHandler {
	
	FileInputStream in;
	FileOutputStream out;
	PrintWriter stdOut;
	BufferedReader stdIn;
	
	/**
	 * Creates a new IO handler which accepts input from standard in
	 * and prints to standard out.
	 */
	public IOHandler() {
		stdOut = new PrintWriter(System.out);
		stdIn = new BufferedReader(new InputStreamReader(System.in));
	}
	
	/**
	 * Creates a new file handler and opens a file for input.
	 * @param fileName The name of the file to open.
	 */
	public IOHandler(String fileName) {
		openFileForInput(fileName);
	}
	
	/**
	 * Opens a file for reading.
	 * @param name The name of the file.
	 */
	public void openFileForInput(String name) {
		try {
			in = new FileInputStream(name);
		} catch (FileNotFoundException fnfe) {
			System.err.println("Error: No input file " +
					"found matching [ " + name + " ] !");
		}
	}
	
	/**
	 * Opens a file for writing, or creates it if it does not exist.
	 * @param name The name of the file.
	 */
	public void openFileForOutput(String name) {
		try {
			File f = new File(name);
			f.createNewFile();
			out = new FileOutputStream(f);
		} catch (IOException e) {
			System.err.println("Error: Could not create or " +
					"use the file " + name + " for output!");
		}
	}
	
	/**
	 * Reads in a given amount of bytes from the file.
	 * @param length The maximum amount of bytes to read in.
	 * @return The bytes read in from the file.
	 */
	public byte[] readBytes(int length) {
		byte[] b = new byte[length];
		try {
			int read = in.read(b);
			if(length != read)
				b = Arrays.copyOf(b, read);
		} catch (IOException ioe) {
			System.err.println
			("Error: Could not read from the file! I/O Error.");
		}
		return b;
	}
	
	/**
	 * Writes bytes out to a file.
	 * @param b The bytes to write out.
	 */
	public void writeBytes(byte[] b) {
		
		try {
			out.write(b);
		} catch (IOException e) {
			System.err.println
			("Error: Could not write to the file! I/O Error.");
		}
	}

}
