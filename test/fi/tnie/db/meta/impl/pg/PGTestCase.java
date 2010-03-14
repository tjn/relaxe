/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fi.tnie.db.QueryException;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.IllegalIdentifierException;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.CatalogFactory;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.SchemaMap;
import fi.tnie.db.meta.impl.common.JDBCTestCase;
import fi.tnie.util.io.Pipe;

public abstract class PGTestCase
	extends JDBCTestCase {
	
	private Catalog catalog = null;
	public static final String SCHEMA_PUBLIC = "public";
	public static final String TABLE_CONTINENT = "continent";
	public static final String TABLE_COUNTRY = "country";

	public PGTestCase() {
		super("org.postgresql.Driver", "tester", "password", "dbmeta_test");	
	}
	

	@Override
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

	
	@Override
	public void restore()
		throws IOException, InterruptedException {
		List<String> args = new ArrayList<String>();
		
		File dump = getTestDump();
		
		if (!dump.canRead()) {
			throw new FileNotFoundException(dump.getAbsolutePath());
		}
		
		dropDatabaseIfExists();		
		createDatabase();		
		
		args.add("pg_restore.exe");
		args.add("-h");
		args.add("localhost");
		args.add("-p");
		args.add("5432");
		args.add("-U");
		args.add(getUserid());
		args.add("--exit-on-error"); 
//		args.add("-C"); // create target database
		args.add("-d");
		args.add(getDatabase());
		
		args.add("-v");
		args.add(getTestDump().getPath());
						
		String[] aa = {};
		aa = args.toArray(aa);
		
		System.err.println(args.toString());
		
		Process p = Runtime.getRuntime().exec(aa);
		
//		System.err.println("waiting...");
		
		Thread ir = new Thread(new Pipe(p.getInputStream(), System.out, Pipe.Endpoint.IN));
		Thread er = new Thread(new Pipe(p.getErrorStream(), System.err, Pipe.Endpoint.IN));
		ir.start();
		er.start();
		
		p.getOutputStream().close();
				
		int exit = p.waitFor();		
				
//		System.err.println("result: " + exit);
		
		if (exit != 0) {
			throw new RuntimeException("restore failed: " + exit);
		}
	}

	protected PGEnvironment newEnv() {
		return new PGEnvironment();
	}
	
	protected Catalog getCatalog() 
		throws QueryException, SQLException {
	
		if (catalog == null) {
			PGEnvironment env = newEnv();
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
		return getCatalog().getEnvironment().createIdentifier(name);
	}
	
	    
    public boolean databaseExists()
       throws IOException, InterruptedException {
        List<String> args = new ArrayList<String>();

        args.add("psql.exe");
        args.add("-h");
        args.add("localhost");
        args.add("-p");
        args.add("5432");
        args.add("-U");
        args.add(getUserid());
        args.add("-l");
                        
        String[] aa = {};
        aa = args.toArray(aa);
        
        System.err.println(args.toString());
        
        Process p = Runtime.getRuntime().exec(aa);
        
        System.err.println("waiting...");
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        Thread ir = new Thread(new Pipe(p.getInputStream(), out, Pipe.Endpoint.IN));
        Thread er = new Thread(new Pipe(p.getErrorStream(), System.err, Pipe.Endpoint.IN));
        ir.start();
        er.start();
        
        p.getOutputStream().close();
                
        int exit = p.waitFor();     
                
        System.err.println("result: " + exit);
        
        if (exit != 0) {
            throw new RuntimeException("unable to find out if the test database exists");
        }
        
        String name = new String(out.toByteArray());
        
        String pattern = " " + getDatabase() + " ";	        
        return (name.indexOf(pattern) >= 0);
    }    
    
    public void dropDatabaseIfExists() 
        throws IOException, InterruptedException {        
        if (databaseExists()) {
            dropDatabase();
        }        
    }
    
    public void dropDatabase()
        throws IOException, InterruptedException {
         List<String> args = new ArrayList<String>();
    
         args.add("dropdb.exe");
         args.add("-h");
         args.add("localhost");
         args.add("-p");
         args.add("5432");
         args.add("-U");
         args.add(getUserid());
         args.add(getDatabase());
                         
         String[] aa = {};
         aa = args.toArray(aa);
         
         System.err.println(args.toString());
         
         Process p = Runtime.getRuntime().exec(aa);
         
         System.err.println("waiting...");
         
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         
         Thread ir = new Thread(new Pipe(p.getInputStream(), out, Pipe.Endpoint.IN));
         Thread er = new Thread(new Pipe(p.getErrorStream(), out, Pipe.Endpoint.IN));
         ir.start();
         er.start();
         
         p.getOutputStream().close();
                 
         int exit = p.waitFor();
         
         String msg = new String(out.toByteArray());
         
         if (exit != 0) {
             throw new RuntimeException(
                     "unable to drop database " + getDatabase() + " [" + exit + "]: " + msg);
         }
         
         logger().info("database dropped: " + getDatabase());
     }   

    public void createDatabase()
        throws IOException, InterruptedException {
     List<String> args = new ArrayList<String>();

     args.add("createdb.exe");
     args.add("-h");
     args.add("localhost");
     args.add("-p");
     args.add("5432");
     args.add("-U");
     args.add(getUserid());
     args.add("-O");
     args.add(getUserid());
     args.add("-T");
     args.add("template0");     
     args.add("-E");
     args.add("UTF-8");     
     
     args.add(getDatabase());
                     
     String[] aa = {};
     aa = args.toArray(aa);
     
     System.err.println(args.toString());
     
     Process p = Runtime.getRuntime().exec(aa);
     
     System.err.println("waiting...");
     
     ByteArrayOutputStream out = new ByteArrayOutputStream();
     
     Thread ir = new Thread(new Pipe(p.getInputStream(), out, Pipe.Endpoint.IN));
     Thread er = new Thread(new Pipe(p.getErrorStream(), out, Pipe.Endpoint.IN));
     ir.start();
     er.start();
     
     p.getOutputStream().close();
             
     int exit = p.waitFor();
     
     String msg = new String(out.toByteArray());
     
     if (exit != 0) {
         throw new RuntimeException(
                 "unable to create database " + getDatabase() + " [" + exit + "]: " + msg);
     }
     
     logger().info("database create: " + getDatabase());
 }   
	
}
