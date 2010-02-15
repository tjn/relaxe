/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class PGTestCase
	extends TestCase {
	
	private Connection connection;
	
	private String userid = "postgres";
	private String passwd = "postgres";
	private String database = "dbmeta_test";
	
	
	@Override
	protected void setUp() throws Exception {			
		super.setUp();
				
		restore();
		
		Class.forName("org.postgresql.Driver");		
		String url = getDatabaseURL();
		this.connection = DriverManager.getConnection(url, getUserid(), getPasswd());
	}
	
	@Override
	protected void tearDown() throws Exception {
		if (this.connection != null) {
			this.connection.close();
			this.connection = null;
		}		
	}
	
	protected Connection getConnection() {
		return connection;
	}

	private String getUserid() {
		return userid;
	}

	private String getPasswd() {
		return passwd;
	}

	private String getDatabase() {
		return database;
	}

	private String getDatabaseURL() {
		return "jdbc:postgresql:" + getDatabase();
	}
	
	protected File testDump() {
		return new File("data/test");
	}
	
	public void restore()
		throws IOException, InterruptedException {
		List<String> args = new ArrayList<String>();
		
		File dump = testDump();
		
		if (!dump.canRead()) {
			throw new FileNotFoundException(dump.getAbsolutePath());
		}

		args.add("pg_restore.exe");
		args.add("-h");
		args.add("localhost");
		args.add("-p");
		args.add("5432");
		args.add("-U");
		args.add(getUserid());
//		args.add("-P");
//		args.add(getPasswd());
		args.add("-d");
		args.add(getDatabase());
		
		args.add("-v");
		args.add(testDump().getPath());
				
		
		String[] aa = {};
		aa = args.toArray(aa);
		
		Process p = Runtime.getRuntime().exec(aa);
		
//		InputStream in = p.getInputStream();
		
		System.err.println("writing passwd...");
				
		OutputStream os = p.getOutputStream();
		os.write(getPasswd().getBytes());
		os.flush();
		os.close();
		
		System.err.println("waiting...");
		
		int exit = p.waitFor();
		
		System.err.println("result: " + exit);
		
		
		
		if (exit != 0) {
			throw new RuntimeException("restore failed");
		}
				
//		pg_restore.exe -h localhost -p 5432 -U postgres -d dbmeta_test -v "D:\share\tnie\project\dbmeta\data\test"
		
	}
	
	
	public void testRestore() 
		throws Exception {
		restore();
	}
	
}
