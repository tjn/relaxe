/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.tools;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.appspot.relaxe.env.util.ResultSetWriter;

import fi.tnie.util.cli.CommandLine;
import fi.tnie.util.cli.Parameter;
import fi.tnie.util.cli.Parser;
import fi.tnie.util.io.IOHelper;

public class KeyGenerationInfo
    extends CatalogTool {
    	
    public static final Parameter QUERY_FILE = new Parameter("query-file");
    private String query;    
    
    /**
     * @param args
     */
    public static void main(String[] args) {        
        System.exit(new KeyGenerationInfo().run(args));
    }
    
    @Override
    public void run()
        throws ToolException {            
      try {                        
          String q = getQuery();             
          
          System.out.println("statement: " + q);
          
          Connection c = getConnection();
          c.setAutoCommit(false);
          
          Statement st = c.createStatement();          
          
          int uc = st.executeUpdate(q, Statement.RETURN_GENERATED_KEYS);          
          System.out.println("update-count: " + uc);          
          ResultSet rs = st.getGeneratedKeys();          
          ResultSetWriter rw = new ResultSetWriter(System.out, false);
          rw.apply(rs);                              
          
          c.commit();
          rs.close();
      }
      catch (Exception e) {
          e.printStackTrace();
          throw new ToolException(-1, e.getMessage(), e);
      }      
    }
    
    @Override
    protected void init(CommandLine cl) 
        throws ToolException {     
        super.init(cl);
                        
        String qf = cl.value(QUERY_FILE);
        String q = null;
        
        try {
	        IOHelper ih = new IOHelper();	    
	        
	        if (qf == null) {
	            // read from stdin:        
	        	String name = Charset.defaultCharset().name();	        	
	            q = ih.read(System.in, name, 4096);            
	        }
	        else {
	            q = ih.read(new File(qf));	            
	        }
        } 
        catch (IOException e) {         
            throw new ToolException(e);
        }
        
        setQuery(q);        
    }    
        
    @Override
    protected void prepare(Parser p) {        
        super.prepare(p);   
        
        Parameter fp = QUERY_FILE;
        
        if (!p.containsParameter(fp.getName())) {        
            p.addParameter(fp);            
        }        
    }

//    private Table getTable(Catalog catalog, String schemaName, String tableName) {
//        if (schemaName == null) {
//            throw new NullPointerException("'schemaName' must not be null");
//        }
//                    
//        Schema schema = catalog.schemas().get(schemaName);
//            
//        if (schema == null) {
//            throw new IllegalArgumentException("no such schema: " + schemaName);
//        }
//        
//        Table table = schema.tables().get(tableName);
//            
//        if (table == null) {
//            throw new IllegalArgumentException("no such table: " + tableName);
//        }
//        
//        return table;
//    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }    
}
