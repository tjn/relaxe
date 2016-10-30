package com.appspot.relaxe.ent.value;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;

public class LongVarBinaryTest extends TestCase {
	
	private static Logger logger = LoggerFactory.getLogger(LongVarBinaryTest.class);	

	public void testToArray1() {
		
		byte[] src = new byte[] { 1, 2, 3 }; 
		
		LongVarBinary.Builder bb = new LongVarBinary.Builder(src.length);
		
		for (byte b : src) {
			assertTrue(bb.add(b));
		}
		
		assertFalse(bb.add((byte) 4));
		
		assertEquals(0, bb.remaining());
		
		logger.info("remaining: {}", bb.remaining());
		
		LongVarBinary lb = bb.newLongVarBinary();
		
		byte[] dest = lb.toArray();
				
		assertNotSame(src, dest);
		
		logger.info("src: {}", Arrays.toString(src));
		logger.info("dest: {}", Arrays.toString(dest));
		
		assertTrue(Arrays.equals(src, dest));
	}
	
	public void testToArray2() {
		
		byte[] src = new byte[] { 1, 2, 3 }; 
		
		LongVarBinary.Builder bb = new LongVarBinary.Builder(src);
		
		assertFalse(bb.add((byte) 4));
		
		assertEquals(0, bb.remaining());
		
		logger.info("remaining: {}", bb.remaining());
		
		LongVarBinary lb = bb.newLongVarBinary();
		
		byte[] dest = lb.toArray();
				
		assertNotSame(src, dest);
		
		logger.info("src: {}", Arrays.toString(src));
		logger.info("dest: {}", Arrays.toString(dest));
		
		assertTrue(Arrays.equals(src, dest));
		
		
	}
	
	
	

}
