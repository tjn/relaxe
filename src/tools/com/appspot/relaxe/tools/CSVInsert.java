/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe.tools;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.meta.Table;
import com.appspot.relaxe.tools.CatalogTool;
import com.appspot.relaxe.tools.ToolConfigurationException;
import com.appspot.relaxe.tools.ToolException;

import com.appspot.relaxe.cli.CommandLine;
import com.appspot.relaxe.cli.Option;
import com.appspot.relaxe.cli.Parameter;
import com.appspot.relaxe.cli.Parser;
import com.appspot.relaxe.cli.SimpleOption;
import com.appspot.relaxe.io.IOHelper;

public class CSVInsert
    extends CatalogTool {
    
    public static final Option OPTION_SCHEMA = 
        new SimpleOption("schema", "s", new Parameter("schema-name"),
        "Use this schema as default if the schema name can not be " +
        "inferred from the name of the input file."        
        );
    
    public static final Parameter FILES = new Parameter("files", 1, null);
        
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
        
        addOption(p, CatalogTool.OPTION_SCHEMA);
        addOption(p, CatalogTool.OPTION_VERBOSE);
                
        Parameter fp = FILES;
        
        if (!p.containsParameter(fp.getName())) {        
            p.addParameter(fp);            
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
