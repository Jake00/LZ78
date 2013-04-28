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
		
		IODummyHandler out1 = new IODummyHandler();
		IOFileHandler in1 = new IOFileHandler();
		in1.openForReading(parsedArgs[1]);
		IOHandler pipe1 = new IOPipe(in1, out1);
		
		IOFileHandler out2 = new IOFileHandler();
		IODummyHandler in2;
		IOHandler pipe2;
		
		if (parsedArgs[0].equals("compress")) {
			System.out.println("Compressing file " + parsedArgs[1] + "...");
			Encoder e = new Encoder(pipe1, Integer.parseInt(parsedArgs[2]));
			e.encode();
			
			out2.openForWriting(parsedArgs[1] + ".lz78");
			in2 = new IODummyHandler(out1.getOutputStreamBytes());
			pipe2 = new IOPipe(in2, out2);
			
			BitPacker bp = new BitPacker(pipe2);
			bp.pack();
		} else if (parsedArgs[0].equals("decompress")) {
			System.out.println("Decompressing file " + parsedArgs[1] + "...");
			BitUnpacker bu = new BitUnpacker(pipe1);
			bu.unpack();
			out2.openForWriting(parsedArgs[1] + ".lz78");
			in2 = new IODummyHandler(out1.getOutputStreamBytes());
			pipe2 = new IOPipe(in2, out2);
			Decoder d = new Decoder(pipe2);
			d.decode();
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
		parsedArgs[2] = "20";
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
		("\nCompressor and decompressor using the LZ78 algorithm.");
		
		System.out.println
		("Usage: lz78 [compress/decompress] [input file]");
		
		System.out.println
		("For compression there is an optional flag, -b [max bits],\n where [max " +
		"bits] is the maximum number of bits to use for encoding each phrase.");
		
		System.exit(0);
	}

}
