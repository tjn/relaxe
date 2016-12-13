package com.appspot.relaxe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.QueryExecutor;
import com.appspot.relaxe.ent.OffsetUnit;
import com.appspot.relaxe.hsqldb.AbstractHSQLDBTestCase;
import com.appspot.relaxe.rdbms.PersistenceContext;
import com.appspot.relaxe.rdbms.hsqldb.HSQLDBImplementation;

public class HSQLDBQueryExecutorOffsetTest extends AbstractHSQLDBTestCase {

	private static Logger logger = LoggerFactory.getLogger(HSQLDBQueryExecutorOffsetTest.class);

	public void testCalculateOffset0() throws Exception {		
		QueryExecutor qe = newQueryExecutor();
		
		final int oo = 0;
		
		long[] ao = {0, 1, 100, Long.MAX_VALUE};
		
		for (final long a : ao) {				
			for (OffsetUnit ou : OffsetUnit.values()) {
				long fo = qe.calculateOffset(oo, a, null, ou);
				assertEquals(0, fo);
			}
		}		
	}
	
	public void testCalculateOffset1a() throws Exception {		
		QueryExecutor qe = newQueryExecutor();
		
		final int oo = -1;
		long available = 123;
						
		long fo = qe.calculateOffset(oo, available, null, OffsetUnit.ELEMENT);
		assertEquals(122, fo);
	}
	
	public void testCalculateOffset1b() throws Exception {		
		QueryExecutor qe = newQueryExecutor();
		
		final int oo = 1;
		long available = 0;
						
		long fo = qe.calculateOffset(oo, available, null, OffsetUnit.ELEMENT);
		assertEquals(1, fo);
	}

	public void testCalculateOffset1c() throws Exception {		
		QueryExecutor qe = newQueryExecutor();		
		final int oo = -10;
		long available = 10;						
		long fo = qe.calculateOffset(oo, available, null, OffsetUnit.ELEMENT);
		assertEquals(0, fo);
	}
	
	public void testCalculateOffset1d() throws Exception {		
		QueryExecutor qe = newQueryExecutor();		
		final int oo = -1;
		long available = 10;
		Integer pageSize = 20;
		long fo = qe.calculateOffset(oo, available, pageSize, OffsetUnit.PAGE);
		assertEquals(0, fo);
	}

	
	public void testCalculateOffset1e() throws Exception {		
		QueryExecutor qe = newQueryExecutor();		
		final long oo = Integer.MAX_VALUE;		
		Integer pageSize = Integer.MAX_VALUE;
		long fo = qe.calculateOffset(oo, null, pageSize, OffsetUnit.PAGE);		
		long expected = oo * oo;		
		assertEquals(expected, fo);
	}

	public void testCalculateOffset1f() throws Exception {		
		QueryExecutor qe = newQueryExecutor();		
		final long oo = Integer.MAX_VALUE;		
		Integer pageSize = Integer.MAX_VALUE;
		long available = oo * oo;
		long fo = qe.calculateOffset(-oo, available, pageSize, OffsetUnit.PAGE);				
		assertEquals(0, fo);
	}

	public void testCalculateOffset2() throws Exception {
		QueryExecutor qe = newQueryExecutor();
		
		final int oo = -1;
		long available = 123;
		int ps = 10;
						
		long fo = qe.calculateOffset(oo, available, ps, OffsetUnit.PAGE);
		assertEquals(120, fo);
	}

	public void testCalculateOffset3() throws Exception {
		QueryExecutor qe = newQueryExecutor();
		
		final int oo = -1;
		long available = 120;
		int ps = 10;
						
		long fo = qe.calculateOffset(oo, available, ps, OffsetUnit.PAGE);
		assertEquals(available - ps, fo);
	}

	public void testCalculateOffset4() throws Exception {
		QueryExecutor qe = newQueryExecutor();
		
		final int oo = -2;
		long available = 120;
		int ps = 10;
						
		long fo = qe.calculateOffset(oo, available, ps, OffsetUnit.PAGE);
		assertEquals(100, fo);
	}

	private QueryExecutor newQueryExecutor() {
		PersistenceContext<HSQLDBImplementation> pc = getPersistenceContext();
		QueryExecutor qe = new QueryExecutor(pc);
		return qe;
	}

	@Override
	public String getDatabase() {
		return "pagila";
	}
}
