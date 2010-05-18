/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.dbmeta.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fi.tnie.db.expr.ddl.DropTable;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.Table;
import fi.tnie.db.meta.util.ResultSetWriter;
import fi.tnie.util.cli.Argument;
import fi.tnie.util.cli.CommandLine;
import fi.tnie.util.cli.Option;
import fi.tnie.util.cli.Parser;
import fi.tnie.util.cli.SimpleOption;
import fi.tnie.util.io.IOHelper;

public class KeyGenerationInfo
    extends CatalogTool {
    
    public static final Argument QUERY_FILE = new Argument("query-file", 1, 1);
    private String query;    
    
    /**
     * @param args
     */
    public static void main(String[] args) {        
        System.exit(new KeyGenerationInfo().run(args));
    }
    
    @Override
    public int run() {
      int result = -1;
      
      try {                        
          String q = getQuery();             
          
          Connection c = getConnection();
          c.setAutoCommit(false);
          
          Statement st = c.createStatement();          
          
          int uc = st.executeUpdate(q, Statement.RETURN_GENERATED_KEYS);
          
          System.out.println("update-count: " + uc);
          
          ResultSet rs = st.getGeneratedKeys();
          
          ResultSetWriter rw = new ResultSetWriter(System.out, false);
                              
          rs.close();
          
          result = 0;
      }
      catch (Exception e) {
          e.printStackTrace();
      }
      
      
      return result;
    }
    
    @Override
    protected void init(CommandLine cl) 
        throws ToolException {     
        super.init(cl);
        
        String qf = cl.value(QUERY_FILE);
        
        if (qf == null) {
            throw new ToolException("no query file specified");            
        }
        
        try {
            String q = new IOHelper().read(new File(qf));
            setQuery(q);
        } 
        catch (IOException e) {         
            e.printStackTrace();
        }
        
        
        
    }    
        
    @Override
    protected void prepare(Parser p) {        
        super.prepare(p);   
        
        Argument a = QUERY_FILE;
        
        if (!p.containsArgument(a.getName())) {        
            p.addArgument(a);            
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
