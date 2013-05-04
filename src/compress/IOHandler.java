package compress;

public interface IOHandler {
	
	public void closeAllStreams();
	
	public void writeBytes(byte[] b);
	
	public void writeBytes(byte[] b, int length);
	
	public int readBytes(byte[] b);
	
	public int readBytes(byte[] b, int offset, int length);
	
	public String readString();
	
	public void writeTuples(byte character, int pos);
}
