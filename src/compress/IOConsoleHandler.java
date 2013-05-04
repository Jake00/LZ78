package compress;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Scanner;

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
		writeBytes(b, b.length);
	}

	@Override
	public void writeBytes(byte[] b, int length) {
		char[] c = new char[length];
		for (int i = 0; i < length; i++) {
			c[i] = (char) b[i];
		}
		out.write(c);
	}

	@Override
	public int readBytes(byte[] b) {
		return readBytes(b, 0, b.length);
	}
	
	

	@Override
	public String readString() {
		byte[] buf = new byte[1024];
		StringBuilder sb = new StringBuilder();

		while (readBytes(buf) > 1) {
			sb.append(new String(buf, Charset.forName("UTF-8")));
		}

		return sb.toString();
	}

	@Override
	public void writeTuples(byte character, int pos) {
		String enc = new String(new byte[] { character }, Charset.forName("UTF-8"));
		String strout = "(" + pos + ", " + enc + ")\n";
		out.write(strout);
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

	@Override
	public int readBytes(byte[] b, int offset, int length) {
		int numRead = 0;
		try {
			char[] buf = new char[length];

			numRead = in.read(buf, offset, length);
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
}
