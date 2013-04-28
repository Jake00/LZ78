package compress;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
public class GiantTupleGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(args[0]));
			Random r = new Random();
			for(int i = 0; i < Integer.parseInt(args[1]); i++){
				char c = (char)(r.nextInt(26) + 'a');
				Integer tupple = r.nextInt(1000);
				writer.write(tupple.toString() + c);
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
