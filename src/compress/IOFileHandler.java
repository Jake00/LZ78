package compress;

import java.io.*;

/**
 * @author Jake Bellamy 1130587 jrb46
 * @author Michael Coleman 1144239 mjc62
 */
public class IOFileHandler implements IOHandler {
	
	FileInputStream in;
	FileOutputStream out;
	
	/**
	 * Creates a new file handler and opens a file for input.
	 * @param filename The name of the file to open.
	 */
	public IOFileHandler(String filename) {
		openForReading(filename);
		String filenameOutput;
		
		String suffix = filename.replaceAll(".+\\.", ".");
		switch (suffix) {
		
		case ".enc":
			filenameOutput = filename.replaceAll("\\.enc", ".lz78");
			break;
			
		case ".lz78":
			filenameOutput = filename.replaceAll("\\.lz78", ".dec");
			break;
			
		case ".dec":
			filenameOutput = filename.replaceAll("\\.dec", "");
			break;
			
		default:
			filenameOutput = filename + ".enc";
			break;
		}
		
		openForWriting(filenameOutput);
	}
	
	/**
	 * Opens a file for reading.
	 * @param name The name of the file.
	 */
	public void openForReading(String name) {
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
	public void openForWriting(String name) {
		try {
			File f = new File(name);
			f.createNewFile();
			out = new FileOutputStream(f);
		} catch (IOException e) {
			System.err.println("Error: Could not create or " +
					"use the file " + name + " for output!");
		}
	}
	
	@Override
	public void closeAllStreams() {
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
	@Override
	public int readBytes(byte[] b) {
		int numRead = 0;
		try {
			numRead = in.read(b);
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
	@Override
	public void writeBytes(byte[] b) {
		try {
			out.write(b);
		} catch (IOException e) {
			System.err.println
			("Error: Could not write to the file! I/O Error.");
		}
	}
	
	@Override
	public void writeTuples(byte character, int pos) {
		try {
			out.write(pos);
			out.write(character);
		} catch (IOException e) {
			System.err.println
			("Error: Could not write to the file! I/O Error.");
		}
	}
	


}
