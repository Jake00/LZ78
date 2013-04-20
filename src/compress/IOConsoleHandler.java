package compress;

import java.io.*;

public class IOConsoleHandler implements IOHandler {

	PrintWriter out;
	BufferedReader in;	
	
	public IOConsoleHandler() {
		in = new BufferedReader(new InputStreamReader(System.in));
		out = new PrintWriter(System.out);
	}
	
	@Override
	public void closeAllStreams() {
		try {
		out.close();
		in.close();
		} catch (IOException e) {
			System.err.println("Error: Could not close the file stream!");
		}
	}

	@Override
	public void writeBytes(byte[] b) {
		char[] c = new char[b.length];
		for (int i = 0; i < b.length; i++) {
			c[i] = (char) b[i];
		}
		out.write(c);
	}

	@Override
	public int readBytes(byte[] b) {
		int numRead = 0;
		try {
			char[] buf = new char[b.length];
			
			numRead = in.read(buf);
			int i = 0;
			for (char c : buf) {
				b[i++] = (byte) (c & 0xFF);
			}
		} catch (IOException e) {
			System.err.println
			("Error: Could not read from the file! I/O Error.");
		}
		return numRead;
	}

	@Override
	public void writeTuples(byte character, int pos) {
		try {
			String enc = new String(new byte[] { character }, "UTF8");
			String strout = "(" + pos + ", " + enc + ") ";
			out.write(strout);
		} catch (UnsupportedEncodingException e) {
			System.err.println("Error: Unsupported encoding! Cannot " +
					"output the mismatched byte as a readable character.");
		}
	}
	
	public String readLine() {
		String line = null;
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.err.println
			("Error: Could not read from the file! I/O Error.");
		}
		return line;
	}
}
