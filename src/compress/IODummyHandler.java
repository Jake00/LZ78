package compress;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Dummy IO Handler used for testing and emulating the programs input and 
 * output. Bytes are read in and output to an internal array.
 * @author Jake Bellamy, Michael Coleman
 *
 */
public class IODummyHandler implements IOHandler {
	private byte[] inStream;
	private int mark;
	private ArrayList<Byte> outStream;
	
	/**
	 * Construct a new dummy IO handler with a given message input that 
	 * this handler will "read" from.
	 * @param s The message that this handler will read.
	 */
	public IODummyHandler(String s) {
		inStream = s.getBytes(Charset.forName("UTF-8"));
		mark = 0;
		outStream = new ArrayList<Byte>(inStream.length);
	}
	
	/**
	 * Construct a new dummy IO handler with a byte array that this
	 * handler will "read" from.
	 * @param in The byte array which this handler will read.
	 */
	public IODummyHandler(byte[] in) {
		inStream = in;
	}
	
	/**
	 * Construct a new dummy IO Handler with only writing capabilities.
	 */
	public IODummyHandler() {
		outStream = new ArrayList<Byte>();
	}
	
	/**
	 * Gets the bytes that have been written out by this handler.
	 * @return The output in bytes.
	 */
	public byte[] getOutputStreamBytes() {
		byte[] out = new byte[outStream.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = outStream.get(i);
		}
		return out;
	}
	
	/**
	 * Gets a sample tuple for decoding or bitpacking.
	 * @param tupleno Which tuple to return.
	 * @return The tuple message.
	 */
	public static String getSampleTuple(int tupleno) {
		switch (tupleno) {
		case 0: //bed spreaders spread spread  spreads on beds
			return "0b0e0d0 0s0p0r2a3e7s4s6r8d11p7e0a3 14r13s4o0n4b2d5 ";
		case 1: //yellow banana green banana red banana blue
			return "0y0e0l3o0w0 0b0a0n8n8 0g0r2e9 7a9a17 13e0d6b10a18b3u2 ";
		case 2: //i wish i was well enough to wish for a wish down a wishing 
				//well. well enough wishing
			return "0i0 0w1s0h2i2w0a0s7e0l11 0e0n0o0u0g5 0t15 3i9h2f15r" +
					"2a7i22 0d15w14 8 21s5i14g10l11.35l2e14o16g18w4h1n17 ";
		default:
			return "";
		}
	}
	
	/**
	 * Gets a sample message for encoding.
	 * @param tupleno Which message to return.
	 * @return The message.
	 */
	public static String getSampleText(int tupleno) {
		switch (tupleno) {
		case 0:
			return "bed spreaders spread spread  spreads on beds";
		case 1:
			return "yellow banana green banana red banana blue";
		case 2:
			return "i wish i was well enough to wish for a wish down a " +
					"wishing well. well enough wishing";
		default:
			return "";
		}
	}
	
	/**
	 * Since there is no real stream being used this method does nothing.
	 */
	@Override
	public void closeAllStreams() { }

	
	@Override
	public void writeBytes(byte[] b) {
		writeBytes(b, b.length);
	}

	@Override
	public void writeBytes(byte[] b, int length) {
		for (int i = 0; i < length; i++) {
			outStream.add(b[i]);
		}
	}

	@Override
	public int readBytes(byte[] b) {
		return readBytes(b, 0, b.length);
	}

	@Override
	public String readString() {
		return new String(Arrays.copyOfRange(inStream, mark, inStream.length), Charset.forName("UTF-8"));
	}

	@Override
	public void writeTuples(byte character, int pos) {
		writeBytes(String.valueOf(pos).getBytes(Charset.forName("UTF-8")));
		writeBytes(new byte[] { character });
		writeBytes(System.getProperty("line.separator").getBytes());
	}

	@Override
	public int readBytes(byte[] b, int offset, int length) {
		int readCount = 0;
		for (int i = 0; i < length && mark < inStream.length; i++) {
			b[i>>offset] = inStream[mark++];
			readCount++;
		}
		return readCount;
	}
}
