/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.dbmeta.tools.pg;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import fi.tnie.util.io.Launcher;
import fi.tnie.util.io.RunResult;

public class PGRestore
{
    private String userid = "tester";
    private String passwd = "password";
    private String database = "dbmeta_test";
    
    private static Logger logger = Logger.getLogger(PGRestore.class);
    
//    public static void main(String[] args) {
//        try {            
//            Class.forName("org.postgresql.Driver");
//            
//            String u = "tester";
//            String p = "password";
//            String d = "dbmeta_test";
//            
//            System.out.println("restoring database: " + d + ": " + new Date());
//            File dump = PGTestCase.dump();            
//            PGRestore pg = new PGRestore(u, p, d);
//            pg.restore(dump);
//            System.out.println("database restored: " + d + ": " + new Date());
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }    
    
    public PGRestore(String userid, String passwd, String database) {
        super();
        this.userid = userid;
        this.passwd = passwd;
        this.database = database;
    }


    public void restore(File dump)
        throws IOException, InterruptedException {
    	List<String> args = new ArrayList<String>();
    		    		
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
		args.add(dump.getPath());
		
        RunResult rr = Launcher.doExec(args);
        
        if (rr.failed()) {
            throw new RuntimeException(
                    "unable to restore database " + 
                    getDatabase() + " [" + rr.getExitCode() + "]: " + rr.getError());
        }		
    						
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
    
         RunResult rr = Launcher.doExec(args);
         
         if (rr.failed()) {
             throw new RuntimeException(
                     "unable to find out if the test database exists: " + rr.getError());
         }

    //     Sample format of the expected output:
    //     List of databases
    //     SQLTypeName     |  Owner   | Encoding 
    //     -------------+----------+----------
    //      dbmeta_test | tester   | UTF8
    //      postgres    | postgres | WIN1252
    //      template0   | postgres | WIN1252
    //      template1   | postgres | WIN1252
    //     (5 rows)
     
         String o = rr.getOutput();
         logger().debug("database list:{\n" + o + "\n}");        
         String nq = Pattern.quote(getDatabase());
         String pattern = "^ " + nq + " .*$";
         Pattern cp = Pattern.compile(pattern, Pattern.MULTILINE);                
         boolean m = cp.matcher(o).find();
         logger().debug(getDatabase() + " found ? " + m);
         return m;
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
      
      RunResult rr = Launcher.doExec(args);
      
      if (rr.failed()) {
          throw new RuntimeException(
                  "unable to drop database " + 
                  getDatabase() + " [" + rr.getExitCode() + "]: " + rr.getError());
      }
                      
      logger().info("database dropped: " + getDatabase());
  }   

 public void createDatabase()
     throws IOException, InterruptedException {
      Launcher launcher = new Launcher();
 
      launcher.add("createdb.exe");
      launcher.add("-h");
      launcher.add("localhost");
      launcher.add("-p");
      launcher.add("5432");
      launcher.add("-U");
      launcher.add(getUserid());
      launcher.add("-O");
      launcher.add(getUserid());
      launcher.add("-T");
      launcher.add("template0");     
      launcher.add("-E");
      launcher.add("UTF-8");     
      
      launcher.add(getDatabase());
           
      RunResult rr = launcher.exec();
                      
      if (rr.failed()) {
          throw new RuntimeException(
                  "unable to create database " + 
                  getDatabase() + " [" + rr.getExitCode() + "]: " + rr.getError());
      }
      
      logger().info("database created: " + getDatabase());
  }

    public String getUserid() {
        return userid;
    }
    
    public void setUserid(String userid) {
        this.userid = userid;
    }
    
    public String getPasswd() {
        return passwd;
    }
    
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
    
    public String getDatabase() {
        return database;
    }
    
    public void setDatabase(String database) {
        this.database = database;
    }

    
    public static Logger logger() {
        return PGRestore.logger;
    }   
}
