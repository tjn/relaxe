/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.dbmeta.tools;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.Table;
import fi.tnie.db.tools.CatalogTool;
import fi.tnie.db.tools.ToolConfigurationException;
import fi.tnie.db.tools.ToolException;
import fi.tnie.util.cli.Argument;
import fi.tnie.util.cli.CommandLine;
import fi.tnie.util.cli.Option;
import fi.tnie.util.cli.Parser;
import fi.tnie.util.cli.SimpleOption;
import fi.tnie.util.io.IOHelper;

public class CSVInsert
    extends CatalogTool {
    
    public static final Option OPTION_SCHEMA = 
        new SimpleOption("schema", "s", new Argument(false),
        "Use this schema as default if the schema name can not be " +
        "determined from the name of the input file."        
        );
    
    public static final Argument FILES = new Argument("files", 1, null);
        
    private String defaultSchema = null;    
    private List<String> files;
    
    /**
     * @param args
     */
    public static void main(String[] args) {        
        System.exit(new CSVInsert().run(args));
    }
    
    @Override
    protected void run()
        throws ToolException {
      String defaultSchema = getDefaultSchema();      
      List<String> files = getFiles();
      
      if (files.isEmpty()) {
          throw new ToolException("no input files");           
      }     
      
      message("files: " + files);
      
      int failures = 0;
      
      for (String file : files) {
          FileReader r = null;
          
          boolean succeeded = false;
          
          try {
              File path = new File(file);              
              message(path.getPath());   
              
              String n = path.getName();                    
              n = n.replace('-', '_');
              
              if (!n.equals(path.getName())) {
                  message("translated file name (" + path.getName() + ") into: " + n);
              }
              
              Pattern np = Pattern.compile("^(?:([^.]+)\\.)?([^.]+)\\.(.+)$", Pattern.CASE_INSENSITIVE);
              
//              System.err.println("pattern: " + np.pattern());
//              System.err.println("file: " + n);
    
              String schema = null;
              String table = null;
    
              Matcher nm = np.matcher(n);
              
              if (nm.find()) {
                  schema = nm.group(1);
                  table = nm.group(2);                                                
                  message("schema derived from file name: " + schema);
                  message("table derived from file name: " + table);
              }
              else {
                  message("name (" + n + ") does not match pattern: " + np.toString());
              }
              
              if (schema == null) {
                  schema = defaultSchema;
                  message("default schema: " + defaultSchema);
              }
              
              if (table == null || schema == null) {
                  message(
                      "unable to determine table or schema " +
                      "from file name " + path.getName() + " " +
                      "(table: " + table + ", " +
                      "schema: " + schema + ").");                  
              }
              else {
                  Catalog cat = getCatalog();
                  Connection c = getConnection();
                  
                  r = new FileReader(path);     
                  Table t = getTable(cat, schema, table);
                                    
                  CSVInsertTask insert = isVerbose() ? 
                      new CSVInsertTask() :
                      new CSVInsertTask() {
                      @Override
                       public void updated(int[] updateCounts) {
                          message("statements executed: " + updateCounts.length);
                          
                          for (int i = 0; i < updateCounts.length; i++) {                            
                            message("rows affected: " + updateCounts[i]);                            
                        } 
                     }                      
                  };
                  
                  insert.run(c, r, t);
                  succeeded = true;
                  
                  if (isVerbose()) {
                      message(path.getPath() + " inserted into " + schema + "." + table);
                  }
              }
          }
          catch(IOException e) {
              message(e.getMessage());              
          }
          catch (SQLException e) {              
              message(e.getMessage());
              SQLException ne = e.getNextException();
              
              while (ne != null) {
                  message(ne.getMessage());
                  ne = ne.getNextException();
              }            
          }
          catch (Throwable e) {
              message(e.getMessage());
          }
          finally {
              IOHelper.doClose(r);
          }
          
          if (!succeeded) {
              failures++;
          }          
      }
      
      message("failures: " + failures);
      
      if (failures > 0) {
          throw new ToolException("failures: " + failures);
      }       
    }
    
    @Override
    protected void init(CommandLine cl) 
        throws ToolException {     
        
        if (cl.values().isEmpty()) {
            throw new ToolConfigurationException("No input files. You need help.");
        }
        
        setFiles(cl.values());
        
        String s = cl.value(OPTION_SCHEMA);
        setDefaultSchema(s);
    }    
        
    @Override
    protected void prepare(Parser p) {        
        super.prepare(p);    
        
        addOption(p, OPTION_SCHEMA);
        addOption(p, OPTION_VERBOSE);
                
        Argument a = FILES;
        
        if (!p.containsArgument(a.getName())) {        
            p.addArgument(a);            
        }        
    }

    public String getDefaultSchema() {
        return defaultSchema;
    }

    public void setDefaultSchema(String defaultSchema) {
        this.defaultSchema = defaultSchema;
    }

    public List<String> getFiles() {
        if (files == null) {
            files = new ArrayList<String>();            
        }

        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
    
    
    private Table getTable(Catalog catalog, String schemaName, String tableName) {
        if (schemaName == null) {
            throw new NullPointerException("'schemaName' must not be null");
        }
                    
        Schema schema = catalog.schemas().get(schemaName);
            
        if (schema == null) {
            throw new IllegalArgumentException("no such schema: " + schemaName);
        }
        
        Table table = schema.tables().get(tableName);
            
        if (table == null) {
            throw new IllegalArgumentException("no such table: " + tableName);
        }
        
        return table;
    }    
}
