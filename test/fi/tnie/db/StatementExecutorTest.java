/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.SQLException;

import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.env.util.ResultSetWriter;
import fi.tnie.db.expr.CountFunction;
import fi.tnie.db.expr.DefaultTableExpression;
import fi.tnie.db.expr.From;
import fi.tnie.db.expr.Select;
import fi.tnie.db.expr.SelectStatement;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.expr.pg.PGInsert;
import fi.tnie.db.gen.pg.ent.LiteralCatalog;
import fi.tnie.db.meta.DBMetaTestCase;
import fi.tnie.db.meta.Table;
import fi.tnie.db.query.QueryException;
import fi.tnie.db.rpc.LongHolder;
import fi.tnie.db.rpc.PrimitiveHolder;

public abstract class StatementExecutorTest<I extends Implementation<I>> 
	extends DBMetaTestCase<I> {	
	
	public void testFetch() throws SQLException, QueryException, EntityException, ClassNotFoundException {
		TestContext<I> tc = getTestContext(null);		
		Connection c = getConnection();
		
		// TODO: try to eliminate need for this:
		LiteralCatalog.getInstance();
		
		StatementExecutor se  = new StatementExecutor(tc.getPersistenceContext());
				
		Select select = new Select();
		select.add(new CountFunction());
		From from = new From(new TableReference(LiteralCatalog.LiteralBaseTable.PUBLIC_ACTOR));
		
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

	public void testPGInsert() throws SQLException, QueryException, EntityException, ClassNotFoundException {
		// PGImplementation pg = new PGImplementation();
		I imp = implementation();
		TestContext<I> tc = getTestContext(imp);		
		Connection c = tc.newConnection();
		LiteralCatalog.getInstance();
		
		StatementExecutor se  = new StatementExecutor(getPersistenceContext());
				
		Table t = LiteralCatalog.LiteralBaseTable.PUBLIC_COUNTRY;		
		// assertNotNull(t.getSchema());
//		assertNotNull(t.getSchema().getUnqualifiedName());
		assertNotNull(t.getName());
		assertNotNull(t.getName().getQualifier());
		assertNotNull(t.getName().getQualifier().getSchemaName());
		assertNotNull(t.getName().getQualifier().getSchemaName().getName());
		PGInsert pgi = new PGInsert(t);
		
		pgi.generate();
		
		ResultSetWriter rw = new ResultSetWriter(System.out, false);
						
		se.execute(pgi, c, rw);
				
		c.close();
	}

}
