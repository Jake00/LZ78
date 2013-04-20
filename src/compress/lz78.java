package compress;

/**
 * @author Jake Bellamy 1130587 jrb46
 * @author Michael Coleman 1144239 mjc62
 */
public class lz78 {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] parsedArgs = parseArguments(args);

		if (parsedArgs[0].equals("compress")) {
			Encoder e;
			if (parsedArgs[2] != null)
				e = new Encoder(parsedArgs[1], Integer.parseInt(parsedArgs[2]));
			else
				e = new Encoder(parsedArgs[1]);
			
			e.encode();
			
		} else if (parsedArgs[0].equals("decompress")) {
			Decoder d = new Decoder(parsedArgs[1]);
			
//			d.decode();
			
		}
		

	}
	
	/**
	 * Takes the program arguments and values that the user specifies, strips the 
	 * argument option and places the corresponding values into a formatted array.
	 * @param args The programs input arguments.
	 * @return A length-3 array of strings with the options (if specified) set in 
	 * the format: 
	 * [0: Compress/Decompress, 1: Input file, 2:Max bits]
	 */
	public static String[] parseArguments(String[] args) {
		String[] parsedArgs = new String[3];
		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
			case "-b":
				Integer.parseInt(args[++i]);
				parsedArgs[2] = args[i];
				break;
				
			case "compress":
				parsedArgs[0] = "compress";
				break;
				
			case "decompress":
				parsedArgs[0] = "decompress";
				break;
				
			default:
				parsedArgs[1] = args[i];
				break;
			}
		}
		if (parsedArgs[1] == null) {
			System.err.println("No input file argument given.");
			help();
		} else if (parsedArgs[0] == null) {
			//If the user did not explicitly specify whether to compress or 
			//decompress then try and guess based on the input file suffix.
			if(parsedArgs[1].endsWith(".lz78")) {
				parsedArgs[0] = "compress";
			} else {
				parsedArgs[0] = "decompress";
			}
		}
		
		return parsedArgs;
	}
	
	/**
	 * Prints a help message to standard output and terminates the program.
	 */
	public static void help() {
		System.out.println
		("Compressor and decompressor using the LZ78 algorithm.");
		
		System.out.println
		("Usage: lz78 [compress/decompress] [input file]");
		
		System.out.println
		("For compression there is an optional flag, -b [max bits], where [max " +
		"bits] is the maximum number of bits to use for encoding each phrase.");
	}

}
