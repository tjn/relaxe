/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.Connection;
import java.sql.SQLException;

import com.appspot.relaxe.StatementExecutor;
import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.EntityException;
import com.appspot.relaxe.env.Implementation;
import com.appspot.relaxe.expr.CountFunction;
import com.appspot.relaxe.expr.DefaultTableExpression;
import com.appspot.relaxe.expr.From;
import com.appspot.relaxe.expr.Select;
import com.appspot.relaxe.expr.SelectStatement;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.meta.DBMetaTestCase;
import com.appspot.relaxe.meta.Table;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.rpc.LongHolder;
import com.appspot.relaxe.rpc.PrimitiveHolder;


public abstract class StatementExecutorTest<I extends Implementation<I>> 
	extends DBMetaTestCase<I> {	
	
	public void testFetch(Table table) throws SQLException, QueryException, EntityException, ClassNotFoundException {
		TestContext<I> tc = getTestContext(null);		
		Connection c = getConnection();
				
		StatementExecutor se  = new StatementExecutor(tc.getPersistenceContext());
				
		Select select = new Select(new CountFunction());		
		From from = new From(new TableReference(table));
		
		DefaultTableExpression qe = new DefaultTableExpression(select, from, null, null);
		SelectStatement ss = new SelectStatement(qe.getQueryExpression());
		
		DataObject result = se.fetchFirst(ss, c);
		assertNotNull(result);
		logger().debug("testFetch: result=" + result);
		
		PrimitiveHolder<?, ?, ?> h = result.get(0);
		logger().debug("testFetch: h=" + h);
		LongHolder lh = h.asLongHolder();
		assertNotNull(lh);
		logger().debug("testFetch: value=" + lh.value());
				
		c.close();
	}

	// TODO: move me
	
//	public void testPGInsert() throws SQLException, QueryException, EntityException, ClassNotFoundException {
//		// PGImplementation pg = new PGImplementation();
//		I imp = implementation();
//		TestContext<I> tc = getTestContext(imp);		
//		Connection c = tc.newConnection();
//		LiteralCatalog.getInstance();
//		
//		StatementExecutor se  = new StatementExecutor(getPersistenceContext());
//				
//		Table t = LiteralCatalog.LiteralBaseTable.PUBLIC_COUNTRY;		
//		// assertNotNull(t.getSchema());
////		assertNotNull(t.getSchema().getUnqualifiedName());
//		assertNotNull(t.getName());
//		assertNotNull(t.getName().getQualifier());
//		assertNotNull(t.getName().getQualifier().getSchemaName());
//		assertNotNull(t.getName().getQualifier().getSchemaName().getName());
//		PGInsert pgi = new PGInsert(t);
//		
//		pgi.generate();
//		
//		ResultSetWriter rw = new ResultSetWriter(System.out, false);
//						
//		se.execute(pgi, c, rw);
//				
//		c.close();
//	}

}
