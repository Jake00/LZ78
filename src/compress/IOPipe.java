package compress;

import java.io.IOException;

public class IOPipe implements IOHandler {
	
	IOHandler in;
	IOHandler out;
	
	public IOPipe(IOHandler in, IOHandler out) {
		this.in = in;
		this.out = out;
	}

	@Override
	public void closeAllStreams() {
		in.closeAllStreams();
		out.closeAllStreams();
	}

	@Override
	public void writeBytes(byte[] b) {
		out.writeBytes(b);
	}

	@Override
	public void writeBytes(byte[] b, int length) {
		out.writeBytes(b, length);
	}

	@Override
	public int readBytes(byte[] b) {
		return in.readBytes(b);
	}

	@Override
	public String readString() {
		return in.readString();
	}

	@Override
	public void writeTuples(byte character, int pos) {
		out.writeTuples(character, pos);
	}
}
