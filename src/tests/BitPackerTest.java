package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import compress.BitPacker;
import compress.IODummyHandler;

public class BitPackerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPack1() {
		//bed spreaders spread spread  spreads on beds
		//40b0e0d0 0s0p0r2a3e7s4s6r8d11p7e0a3 14r13s4o0n4b2d5 
		String s = IODummyHandler.getSampleTuple(0);
		IODummyHandler io = new IODummyHandler(s);
		BitPacker bp = new BitPacker(io);
		bp.pack();
		byte[] packed = io.getOutputStreamBytes();
		byte[] expected = new byte[] {
				(byte) 0b00000001, (byte) 0b10001000, (byte) 0b00011001, (byte) 0b01000001, 
				(byte) 0b10010000, (byte) 0b00001000, (byte) 0b00000001, (byte) 0b11001100,
				(byte) 0b00011100, (byte) 0b00000001, (byte) 0b11001000, (byte) 0b10100110, 
				(byte) 0b00010010, (byte) 0b11011001, (byte) 0b01001111, (byte) 0b10111001,
				(byte) 0b10011100, (byte) 0b01110011, (byte) 0b00111100, (byte) 0b11100100,
				(byte) 0b10010000, (byte) 0b11001000, (byte) 0b10010110, (byte) 0b11100000,
				(byte) 0b01111101, (byte) 0b10010100, (byte) 0b00011000, (byte) 0b01001011, 
				(byte) 0b00100000, (byte) 0b01001110, (byte) 0b01110010, (byte) 0b01001101,
				(byte) 0b01110011, (byte) 0b00111000, (byte) 0b11011110, (byte) 0b00001101, 
				(byte) 0b11000111, (byte) 0b00011000, (byte) 0b10001010, (byte) 0b01100100,
				(byte) 0b00111010, (byte) 0b00000000
		};
		assertArrayEquals(expected, packed);
	}

	@Test
	public void testPack2() {
		//yellow banana green banana red banana blue
		//40y0e0l3o0w0 0b0a0n8n8 0g0r2e9 7a9a17 13e0d6b10a18b3u2 
		String s = IODummyHandler.getSampleTuple(1);
		IODummyHandler io = new IODummyHandler(s);
		BitPacker bp = new BitPacker(io);
		bp.pack();
		byte[] packed = io.getOutputStreamBytes();
		byte[] expected = new byte[] {
				(byte) 0b00000001, (byte) 0b11100100, (byte) 0b00011001, (byte) 0b01000001, 
				(byte) 0b10110000, (byte) 0b10110110, (byte) 0b11110000, (byte) 0b01110111, 
				(byte) 0b00000010, (byte) 0b00000000, (byte) 0b01100010, (byte) 0b00000110, 
				(byte) 0b00010000, (byte) 0b01101110, (byte) 0b01001000, (byte) 0b01101110, 
				(byte) 0b01001000, (byte) 0b00100000, (byte) 0b00000110, (byte) 0b01110000,
				(byte) 0b01110010, (byte) 0b00101001, (byte) 0b10010101, (byte) 0b00100100, 
				(byte) 0b10000000, (byte) 0b11111011, (byte) 0b00001010, (byte) 0b01001011, 
				(byte) 0b00001010, (byte) 0b11000100, (byte) 0b10000001, (byte) 0b00110101, 
				(byte) 0b10010100, (byte) 0b00011001, (byte) 0b00001111, (byte) 0b00110001, 
				(byte) 0b00100101, (byte) 0b00110000, (byte) 0b10101100, (byte) 0b10011000,
				(byte) 0b10001011, (byte) 0b01110101, (byte) 0b00101000, (byte) 0b00000000
		};
		assertArrayEquals(expected, packed);
	}
	
	@Test
	public void testPack3() {
		//i wish i was well enough to wish for a wish down a wishing well. well enough wishing
		//40i0 0w1s0h2i2w0a0s7e0l11 0e0n0o0u0g5 0t15 3i9h2f15r2a7i22 0d15w14 8 21s5i14g10l11.35l2e14o16g18w4h1n17 
		
		String s = IODummyHandler.getSampleTuple(2);
		IODummyHandler io = new IODummyHandler(s);
		BitPacker bp = new BitPacker(io);
		bp.pack();
		byte[] packed = io.getOutputStreamBytes();
		byte[] expected = new byte[] {
				(byte) 0b00000001, (byte) 0b10100100, (byte) 0b00001000, (byte) 0b00000001, 
				(byte) 0b11011100, (byte) 0b01101110, (byte) 0b01100000, (byte) 0b11010000, 
				(byte) 0b01010011, (byte) 0b01001001, (byte) 0b01001110, (byte) 0b11100000, 
				(byte) 0b11000010, (byte) 0b00001110, (byte) 0b01100111, (byte) 0b11011001, 
				(byte) 0b01000001, (byte) 0b10110001, (byte) 0b00101100, (byte) 0b10000000, 
				(byte) 0b00011001, (byte) 0b01000001, (byte) 0b10111000, (byte) 0b00011011, 
				(byte) 0b11000001, (byte) 0b11010100, (byte) 0b00011001, (byte) 0b11001110, 
				(byte) 0b10010000, (byte) 0b00000011, (byte) 0b10100010, (byte) 0b01111001, 
				(byte) 0b00000001, (byte) 0b01101101, (byte) 0b00101001, (byte) 0b00101101, 
				(byte) 0b00000101, (byte) 0b00110011, (byte) 0b00100111, (byte) 0b10111001, 
				(byte) 0b00010100, (byte) 0b11000010, (byte) 0b01111101, (byte) 0b10100101, 
				(byte) 0b01101100, (byte) 0b01000000, (byte) 0b00001100, (byte) 0b10001001, 
				(byte) 0b11101110, (byte) 0b11101001, (byte) 0b11000100, (byte) 0b00001001, 
				(byte) 0b00000100, (byte) 0b00001011, (byte) 0b01010111, (byte) 0b00110011, 
				(byte) 0b10101101, (byte) 0b00101001, (byte) 0b11001100, (byte) 0b11101001, 
				(byte) 0b01001101, (byte) 0b10001001, (byte) 0b01100101, (byte) 0b11001101, 
				(byte) 0b00011011, (byte) 0b01100001, (byte) 0b01001100, (byte) 0b10101001, 
				(byte) 0b11001101, (byte) 0b11101011, (byte) 0b00000110, (byte) 0b01110101, 
				(byte) 0b10010011, (byte) 0b10111001, (byte) 0b11000110, (byte) 0b10000001, 
				(byte) 0b10110111, (byte) 0b00101100, (byte) 0b01000000, (byte) 0b00000000
		};
		assertArrayEquals(expected, packed);
	}
}
