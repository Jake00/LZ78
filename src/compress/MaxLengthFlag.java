package compress;

public enum MaxLengthFlag {
	LENGTH_4 (0b00), 
	LENGTH_5 (0b01), 
	LENGTH_6 (0b10);
	
	private final int length;
	
	MaxLengthFlag(int len) {
		length = len;
	}
}
