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
	boolean filein;
	boolean fileout;
	
	/**
	 * Creates a new IO handler which accepts input from standard in
	 * and prints to standard out.
	 */
	public IOHandler() {
		stdOut = new PrintWriter(System.out);
		stdIn = new BufferedReader(new InputStreamReader(System.in));
		filein = false;
		fileout = false;
	}
	
	/**
	 * Creates a new file handler and opens a file for input.
	 * @param fileName The name of the file to open.
	 */
	public IOHandler(String fileName) {
		openFileForInput(fileName);
		openFileForOutput(fileName + ".enc");
	}
	
	/**
	 * Opens a file for reading.
	 * @param name The name of the file.
	 */
	public void openFileForInput(String name) {
		try {
			in = new FileInputStream(name);
			filein = true;
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
			fileout = true;
		} catch (IOException e) {
			System.err.println("Error: Could not create or " +
					"use the file " + name + " for output!");
		}
	}
	
	public void closeFileStreams() {
		try {
			in.close();
			out.close();
		} catch (IOException ioe) {
			System.err.println("Error: Could not close the file stream!");
		}
	}
	
	/**
	 * Reads in a given amount of bytes from the input stream.
	 * @param length The maximum amount of bytes to read in.
	 * @return The bytes read in from the file.
	 */
	public int readBytes(byte[] b) {
		int numRead = 0;
		try {
			if (filein) {
				numRead = in.read(b);
			} else {
				char[] buf = new char[b.length];
				numRead = stdIn.read(buf);
				int i = 0;
				for (char c : buf) {
					b[i++] = (byte) (c & 0xFF);
				}
			}
		} catch(IOException ioe) {
			System.err.println
			("Error: Could not read from the file! I/O Error.");
		}
		return numRead;
	}
	
	/**
	 * Writes bytes out to the output stream.
	 * @param b The bytes to write out.
	 */
	public void writeBytes(byte[] b) {
		try {
			int pos = (b[0] << 24) | (b[1] << 16) | (b[2] << 8) | b[3];
			if (fileout) {
				out.write(String.valueOf(pos).getBytes("UTF8"));
				out.write(b, 4, b.length-4);
			} else {
				
				String formatted = "(" + pos + ", " + new String(new byte[]{ b[4] }, "UTF8") + ") ";
				stdOut.write(formatted);
				stdOut.flush();
			}
		} catch (IOException e) {
			System.err.println
			("Error: Could not write to the file! I/O Error.");
		}
	}

}
