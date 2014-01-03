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
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.appspot.relaxe.env.util.ResultSetWriter;

import com.appspot.relaxe.cli.CommandLine;
import com.appspot.relaxe.cli.Parameter;
import com.appspot.relaxe.cli.Parser;
import com.appspot.relaxe.io.IOHelper;

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
