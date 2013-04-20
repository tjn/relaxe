/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.mysql;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fi.tnie.db.env.CatalogFactory;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.mysql.MySQLImplementation;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.IllegalIdentifierException;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.SchemaMap;
import fi.tnie.db.meta.impl.common.JDBCTestCase;
import fi.tnie.db.query.QueryException;
import fi.tnie.util.io.Pipe;

public abstract class MySQLJDBCTestCase
	extends JDBCTestCase {
	
	private Catalog catalog = null;
	public static final String SCHEMA_PUBLIC = "public";
	public static final String TABLE_CONTINENT = "continent";
	public static final String TABLE_COUNTRY = "country";
	
	private Implementation<?> implementation;

	public MySQLJDBCTestCase() {
		super("com.mysql.jdbc.Driver", "tester", "password", "dbmeta_test", null);	
	}

	@Override
	public String getDatabaseURL() {
		return "jdbc:mysql://127.0.0.1:3306/" + getDatabase();
	}
	
	protected File getTestDump() {
		File d = dump(getClass());
		
		if (!d.exists()) {
			d = dump(MySQLJDBCTestCase.class);		
		}
		
		return d;
	}
		
	private File dump(Class<?> t) {
		String pkg = MySQLJDBCTestCase.class.getPackage().getName();
		return new File("testdata/" + pkg + "/" + t.getName());
	}

	
	@Override
	public void restore()
		throws IOException, InterruptedException {
		List<String> args = new ArrayList<String>();
		
		File dump = getTestDump();
		
		if (!dump.canRead()) {
			throw new FileNotFoundException(dump.getAbsolutePath());
		}

		args.add("mysql.exe");
		args.add("-h");
		args.add("localhost");
		args.add("-p");
		args.add("5432");
		args.add("-U");
		args.add(getUserid());
		args.add("--exit-on-error"); 
		// args.add("-C"); // create target database		
		args.add("-c"); // clean schema before restoring
		args.add("-d");
		args.add(getDatabase());
		
		args.add("-v");
		args.add(getTestDump().getPath());
						
		String[] aa = {};
		aa = args.toArray(aa);
		
		System.err.println(args.toString());
		
		Process p = Runtime.getRuntime().exec(aa);
		
		System.err.println("waiting...");
		
		Thread ir = new Thread(new Pipe(p.getInputStream(), System.out, Pipe.Endpoint.IN));
		Thread er = new Thread(new Pipe(p.getErrorStream(), System.err, Pipe.Endpoint.IN));
		ir.start();
		er.start();
		
		p.getOutputStream().close();
				
		int exit = p.waitFor();		
				
		System.err.println("result: " + exit);
		
		if (exit != 0) {
			throw new RuntimeException("restore failed");
		}
	}

	protected MySQLImplementation newEnv() {
		return new MySQLImplementation();
	}

	@Override
	protected Catalog getCatalog() 
		throws QueryException, SQLException {
	
		if (catalog == null) {
			MySQLImplementation env = newEnv();
			
			CatalogFactory cf = env.catalogFactory();
			
			assertNotNull(cf);		
			Connection c = getConnection();
			
			assertNotNull(c);
			
			this.catalog = cf.create(c);
			assertNotNull(catalog);
			
		}
		return catalog;
	}

	protected BaseTable getContinentTable() 
		throws QueryException, SQLException {
		return getWellKnownBaseTable(SCHEMA_PUBLIC, TABLE_CONTINENT);
	}

	protected BaseTable getCountryTable() 
		throws QueryException, SQLException {
		return getWellKnownBaseTable(SCHEMA_PUBLIC, TABLE_COUNTRY);
	}

	protected BaseTable getWellKnownBaseTable(String schema, String table) 
		throws QueryException, SQLException {
		SchemaMap sm = getCatalog().schemas();
		assertNotNull(sm);				
		Schema pub = sm.get(schema);
		assertNotNull(sm);
		BaseTable t = pub.baseTables().get(table);
		assertNotNull(t);		
		return t;
	}

	protected Identifier id(String name) 
		throws IllegalIdentifierException, NullPointerException, QueryException, SQLException {
		return getCatalog().getEnvironment().getIdentifierRules().toIdentifier(name);
	}	
	
	
	@Override
	protected Implementation<?> getImplementation() {
		if (implementation == null) {
			implementation = new MySQLImplementation();			
		}

		return implementation;
	}
	
	
}
