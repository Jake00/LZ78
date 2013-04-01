package compress;

/**
 * @author Jake Bellamy 1130587 jrb46
 * @author Michael Coleman 1144239 mjc62
 */
public class Encoder extends Encode {

	public Encoder() {
		System.out.println("yay");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public void encode() {
		/**
		 * Current prefix (P): the prefix currently being processed in the 
		 * encoding algorithm.
		 * Current character (C): a character determined in the encoding algorithm. 
		 * Generally this is the character preceded by the current prefix.
		 * 
		 * The encoding algorithm
		 * 1. At the start, the dictionary and P are empty; 
		 * 2. Assign C next character in the charstream;
		 * 3. Is the string P+C present in the dictionary?
		 * 		a. If YES assign P = P+C (extend P with C);
		 * 		b. If NO,
		 * 			i. Output these two objects to the codestream:
		 * 				- The code word corresponding to P (if P is empty, output a zero);
		 * 				- C, in the same form as input from the charstream;
		 * 			ii. Add the string P+C to the dictionary;
		 * 			iii. Assign P to be empty;
		 * 		c. Are there more characters in the charstream?
		 * 			- If YES return to step 2;
		 * 			- If NO,
		 * 				i. If P is not empty, output the code word corresponding to P;
		 * 				ii. END
		 */
		
		
	}

}
