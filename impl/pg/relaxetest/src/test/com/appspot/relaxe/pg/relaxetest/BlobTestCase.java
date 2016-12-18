package com.appspot.relaxe.pg.relaxetest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.gen.pg.relaxetest.ent.pub.BlobTest;
import com.appspot.relaxe.pg.relaxetest.test.AbstractRelaxetestTestCase;
import com.appspot.relaxe.service.BinaryAttributeReader;
import com.appspot.relaxe.service.BinaryAttributeWriter;
import com.appspot.relaxe.service.ClosableDataAccessSession;
import com.appspot.relaxe.service.EntitySession;


public class BlobTestCase
	extends AbstractRelaxetestTestCase {
	
	private static Logger logger = LoggerFactory.getLogger(BlobTestCase.class);
		
	public void testBlob() throws Exception {
		Integer tid = null;
		
		byte[] content = new byte[1024];
		
		{		
			ClosableDataAccessSession das = newSession();
			BinaryAttributeWriter bw = das;
			
			EntitySession es = das.asEntitySession();
			
			BlobTest bte = es.newEntity(BlobTest.Type.TYPE);
			BlobTest inserted = es.insert(bte);
			
			tid = inserted.getId().value();
			logger.info("test-record: {}", tid);
					
			ByteArrayInputStream in = new ByteArrayInputStream(content);
									
			bw.write(inserted, BlobTest.BLOB_OID.name(), in);
					
			das.commit();
			das.close();
		}

		{
			ClosableDataAccessSession das = newSession();
			BinaryAttributeReader br = das;
			
			EntitySession es = das.asEntitySession();
			
			BlobTest.Mutable bte = es.newEntity(BlobTest.Type.TYPE);
			bte.setId(tid);
											
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
									
			long n = br.read(bte, BlobTest.BLOB_OID.name(), buf);
			logger.info("read: {}", n);
			
			byte[] result = buf.toByteArray();
			
			logger.info("read (2): {}", result.length);
					
			das.commit();
			das.close();
			
			// Assert.assertEquals(content, result);
			
		}
	}

}
