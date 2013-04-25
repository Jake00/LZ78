package tests;

import static org.junit.Assert.*;

import java.nio.charset.Charset;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import compress.Encoder;
import compress.IODummyHandler;

public class EncoderTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEncode1() {
		String s = IODummyHandler.getSampleText(0);
		IODummyHandler io = new IODummyHandler(s);
		Encoder e = new Encoder(io);
		e.encode();
		byte[] encoded = io.getOutputStreamBytes();
		byte[] expected = IODummyHandler.getSampleTuple(0).getBytes(Charset.forName("UTF-8"));
		
		assertArrayEquals(expected, encoded);
	}

	@Test
	public void testEncode2() {
		String s = IODummyHandler.getSampleText(1);
		IODummyHandler io = new IODummyHandler(s);
		Encoder e = new Encoder(io);
		e.encode();
		byte[] encoded = io.getOutputStreamBytes();
		byte[] expected = IODummyHandler.getSampleTuple(1).getBytes(Charset.forName("UTF-8"));
		
		assertArrayEquals(expected, encoded);
	}
	
	@Test
	public void testEncode3() {
		String s = IODummyHandler.getSampleText(2);
		IODummyHandler io = new IODummyHandler(s);
		Encoder e = new Encoder(io);
		e.encode();
		byte[] encoded = io.getOutputStreamBytes();
		byte[] expected = IODummyHandler.getSampleTuple(2).getBytes(Charset.forName("UTF-8"));
		
		assertArrayEquals(expected, encoded);
	}
}
