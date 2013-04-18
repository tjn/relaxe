/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.mysql.samples;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.PersistenceManager;
import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Content;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.mysql.MySQLImplementation;
import fi.tnie.db.env.util.ResultSetWriter;
import fi.tnie.db.mysql.test.AbstractMySQLTestCase;
import fi.tnie.db.query.QueryException;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public class MySQLSamplesMetaTest
	extends AbstractMySQLTestCase {
	
	@Override
	protected PersistenceContext<MySQLImplementation> createPersistenceContext() {
		return new MySQLSamplesTriggerTestPersistenceContext();
	}
		
	@Override
	public String getDatabase() {
		return "samples";
	}	
	
	@Override
	public String getHost() {
		return "127.0.0.1";
	}
	
	
	public void testPrimaryKeys() throws SQLException, ClassNotFoundException, IOException, QueryException {
		
		Connection c = getContext().newConnection();
		
				
		DatabaseMetaData meta = c.getMetaData();
		
		logger().debug("testPrimaryKeys: meta.getURL()=" + meta.getURL());
		
		{
			ResultSet rs = meta.getTables(null, null, "%", new String[] {"TABLE"} );
			ResultSetWriter w = new ResultSetWriter(System.out, false);
			w.apply(rs, true);
			logger().debug("tables: count: " + w.processed());
		}
		
		{		
			ResultSet rs = meta.getPrimaryKeys("samples", null, "abc");		
			ResultSetWriter w = new ResultSetWriter(System.out, false);
			w.apply(rs, true);			
			logger().debug("testPrimaryKeys: count: " + w.processed());
		}		
	}

}
