package com.appspot.relaxe.pg.relaxetest;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.ent.EntityException;
import com.appspot.relaxe.gen.pg.relaxetest.ent.pub.File;
import com.appspot.relaxe.gen.pg.relaxetest.ent.pub.FileBlock;
import com.appspot.relaxe.pg.relaxetest.test.AbstractRelaxetestTestCase;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.service.ClosableDataAccessSession;
import com.appspot.relaxe.service.DataAccessException;
import com.appspot.relaxe.service.EntitySession;
import com.appspot.relaxe.value.IntegerHolder;
import com.appspot.relaxe.value.LongVarBinaryHolder;
import com.appspot.relaxe.value.VarcharHolder;

public class FileReadTest
	extends AbstractRelaxetestTestCase {
	
	private static final class TestOutputStream extends OutputStream {
		
		private long count;
		private MessageDigest digest;
		
		public TestOutputStream(MessageDigest messageDigest)  {
			this.digest = messageDigest;
		}
		
		@Override
		public void write(int b) throws IOException {
			count++;
			this.digest.update((byte) b);			
		}
		
		public byte[] getDigest() {			
			return this.digest.digest();
		}
		
		public long getCount() {
			return count;
		}
	}

	private static final int ONE_MB = 1024 * 1024;

	private static Logger logger = LoggerFactory.getLogger(FileReadTest.class);
	
	public void testBinaryAttributeRead() throws IOException, DataAccessException, EntityException, QueryException, NoSuchAlgorithmException, InterruptedException {
				
		Runtime rt = Runtime.getRuntime();
		
		dumpMemoryUsage("start", rt);
		
		ClosableDataAccessSession das = newSession();
		
		
		final int nfull = 64;		
		final int total = nfull * ONE_MB + ONE_MB / 2;
				
		final int blockSize = 2 * ONE_MB;
		
		
		
		int nfullblock = total / blockSize;
		
		logger.info("nfullblock: {}", nfullblock);
		
				
		final String nm = FileReadTest.class.getName();
		
		int remaining = total - nfullblock * blockSize;
				
		final byte[] last = remaining > 0 ? new byte[remaining] : new byte[0];
				
		
		try {
//			Thread.sleep(10000);
			
			EntitySession es = das.asEntitySession();
			
			delete(das, File.Type.TYPE, File.Attribute.NAME, VarcharHolder.valueOf(nm));						
						
			File.Mutable mf = es.newEntity(File.Type.TYPE);
			mf.setName(FileReadTest.class.getName());
			mf.setBlockSize(blockSize);
			
			File nf = es.insert(mf.as());
			
			logger.info("");
									
			byte[] buf = new byte[blockSize];
			
			int index = 0;
			
			FileBlock.Mutable mb = es.newEntity(FileBlock.Type.TYPE);
			
			MessageDigest id = MessageDigest.getInstance("MD5");
			
			for (int i = 0; i < nfullblock; i++) {				
				mb.setId(IntegerHolder.NULL_HOLDER);
				mb.setFile(nf);
				mb.setIndex(index++);								
				mb.setContent(LongVarBinaryHolder.valueOf(buf));				
				es.insert(mb);
				id.update(buf);
			}
			
			{
				mb.setId(IntegerHolder.NULL_HOLDER);
				mb.setFile(nf);
				mb.setIndex(index++);												
				mb.setContent(LongVarBinaryHolder.valueOf(last));				
				es.insert(mb);
				id.update(last);
			}
			
			dumpMemoryUsage("committing", rt);
			
			das.commit();
			
			dumpMemoryUsage("committed", rt);
			
			
			FileBlock.QueryElement qe = new FileBlock.QueryElement(FileBlock.CONTENT);			
			FileBlock.Query.Builder qb = new FileBlock.Query.Builder(qe);
			qb.addPredicate(qe.newEquals(FileBlock.FILE, nf));
			qb.addSortKey(qe.newSortKey(FileBlock.INDEX, true));
			FileBlock.Query fq = qb.newQuery();
			
			
			final MessageDigest sd = MessageDigest.getInstance("MD5");
					
			TestOutputStream os = new TestOutputStream(sd);
			
			long nread = das.read(fq, FileBlock.CONTENT, os);
			
			dumpMemoryUsage("all read", rt);			
			
			logger.info("total: {}", total);
			logger.info("nread: {}", nread);
			logger.info("total - nread: {}", total - nread);
			logger.info("last.length: {}", last.length);
			logger.info("os.count: {}", os.getCount());			
			
			assertEquals(total, nread);
			assertEquals(total, os.getCount());			
			
			byte[] ih = id.digest();
			byte[] oh = sd.digest();
			
			logger.info("ih: {}", toHex(ih));
			logger.info("oh: {}", toHex(ih));
						
			assertTrue(MessageDigest.isEqual(ih,  oh));
			
		}
		finally {
			das.close();
		}
	}
	
	String toHex(byte[] bytes) {
		StringBuilder buf = new StringBuilder();
		for (byte b : bytes) {			
			String s = Integer.toHexString(b);
			
			if (s.length() == 1) {
				buf.append('0');
			}
			
			buf.append(s);
		}
	
		return buf.toString();
	}

	private void dumpMemoryUsage(String prefix, Runtime rt) {
		long mm = rt.maxMemory();
		long mmb = mm / ONE_MB;		
		
		long free = rt.freeMemory();		
		long used = rt.totalMemory() - free;			
		
		long um = used / ONE_MB;
		long fm = free / ONE_MB;
				
		logger.info("{} : max: {} | user: {} | free: {}", prefix, mmb, um, fm);
	}
	

}
