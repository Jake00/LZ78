package compress;

public interface IOHandler {
	
	public void closeAllStreams();
	
	public void writeBytes(byte[] b);
	
	public int readBytes(byte[] b);
	
	public void writeTuples(byte character, int pos);
}
