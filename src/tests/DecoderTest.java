package tests;

import static org.junit.Assert.*;

import java.nio.charset.Charset;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import compress.Decoder;
import compress.Encoder;
import compress.IODummyHandler;
import compress.IOPipe;

public class DecoderTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDecode() {
		for (int i = 0; i < 3; i++) {
			String textin = IODummyHandler.getSampleText(i);
			IODummyHandler encoderIn = new IODummyHandler(textin);
			IODummyHandler encoderOut = new IODummyHandler();
			IOPipe pipe1 = new IOPipe(encoderIn, encoderOut);

			Encoder e = new Encoder(pipe1, 20);
			e.encode();

			IODummyHandler decoderIn = new IODummyHandler
					(encoderOut.getOutputStreamBytes());
			IODummyHandler decoderOut = new IODummyHandler();
			IOPipe pipe2 = new IOPipe(decoderIn, decoderOut);

			Decoder d = new Decoder(pipe2);
			d.decode();

			String decodedMessage = 
					new String(decoderOut.getOutputStreamBytes(), 
							Charset.forName("UTF-8"));

			assertEquals(textin, decodedMessage);
		}
	}

}
