package compress;

/**
 * @author Jake Bellamy 1130587 jrb46
 * @author Michael Coleman 1144239 mjc62
 */
public class BitPacker {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public void pack() {
		
	}
	
	private int getBitLength(int num) {
		return (32 - Integer.numberOfTrailingZeros(num));
	}

}
