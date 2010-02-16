/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fi.tnie.db.meta.impl.common.JDBCTestCase;
import fi.tnie.util.io.Pipe;

public class PGTestCase
	extends JDBCTestCase {
	
	public PGTestCase() {
		super("org.postgresql.Driver", "tester", "password", "dbmeta_test");	
	}

	public String getDatabaseURL() {
		return "jdbc:postgresql:" + getDatabase();
	}
	
	protected File getTestDump() {
		File d = dump(getClass());
		
		if (!d.exists()) {
			d = dump(PGTestCase.class);		
		}
		
		return d;
	}
		
	private File dump(Class<?> t) {
		String pkg = PGTestCase.class.getPackage().getName();
		return new File("testdata/" + pkg + "/" + t.getName());
	}

	
	public void restore()
		throws IOException, InterruptedException {
		List<String> args = new ArrayList<String>();
		
		File dump = getTestDump();
		
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
	
	public void testRestore() 
		throws Exception {
		restore();
	}
	
	
	
}
