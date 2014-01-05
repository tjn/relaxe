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
/**
 * 
 */
package com.appspot.relaxe.tools;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;

import com.appspot.relaxe.AssignmentVisitor;
import com.appspot.relaxe.QueryHelper;
import com.appspot.relaxe.ValueAssignerFactory;
import com.appspot.relaxe.expr.ElementList;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.InsertStatement;
import com.appspot.relaxe.expr.MutableValueParameter;
import com.appspot.relaxe.expr.ValueRow;
import com.appspot.relaxe.expr.ValuesListElement;
import com.appspot.relaxe.expr.ddl.types.SQLTypeDefinition;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ColumnMap;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.Table;
import com.appspot.relaxe.rpc.VarcharHolder;
import com.appspot.relaxe.tools.CatalogTool;
import com.appspot.relaxe.types.VarcharType;


class CSVInsertTask
    extends CatalogTool {
	
	
	private static class VarcharParameter 
		extends MutableValueParameter<String, VarcharType, VarcharHolder> {

		private static final long serialVersionUID = 785444282283211L;

		public VarcharParameter(Column column, VarcharHolder value) {
			super(column, value);
		}		
	}
        

    public void run(Connection connection, Reader input, Table table) 
        throws IOException, SQLException {                
        
        if (connection == null) {
            throw new NullPointerException("'connection' must not be null");
        }
        
        if (input == null) {
            throw new NullPointerException("'input' must not be null");
        }
        
        if (table == null) {
            throw new NullPointerException("'table' must not be null");
        }
        
        boolean committed = false;
        
        try {                
            connection.setAutoCommit(false);        
            CSVStrategy cs = new CSVStrategy('\t', '"', CSVStrategy.COMMENTS_DISABLED, false, false, false);        
            CSVParser p = new CSVParser(input, cs);
                    
            // get header line
            String[] line = p.getLine();
            
            // configure by using the column headers:        
            ColumnMap cm = table.getColumnMap();
            List<Identifier> names = new ArrayList<Identifier>();  
            List<Column> columnList = new ArrayList<Column>();
                                                                    
            for (String n : line) {
                Column column = cm.get(n);
                
                if (column == null) {
                    throw new IllegalArgumentException("column not found " + n);
                }
                
                columnList.add(column);            
                names.add(column.getColumnName());
            }
            
            if (names.isEmpty()) {
                throw new IllegalStateException("no column names available"); 
            }        
    
            final int expectedColumnCount = line.length;
//            int recno = 0;        
            
            PreparedStatement ps = null;
            InsertStatement ins = null;
            
            VarcharParameter[] params = new VarcharParameter[expectedColumnCount];            
            ValueAssignerFactory vaf = getImplementation().getValueAssignerFactory();
            
            AssignmentVisitor pa = null;
                                    
            while ((line = p.getLine()) != null) {
//                recno++;
                final int cols = line.length;
                int lineno = p.getLineNumber();
                
                if (cols != expectedColumnCount) {
                    throw new IllegalStateException("unexpected column count: " + cols + " at line " + lineno);
                }
                           
                if (ps == null) {
                	
                	List<ValuesListElement> vl = new ArrayList<ValuesListElement>(params.length);
                	
                    for (int i = 0; i < params.length; i++) {                      
                        Column column = columnList.get(i);
                        VarcharHolder h = parse(column, line[i]);     
                        
                        VarcharParameter param = new VarcharParameter(column, h);                    
                        params[i] = param;
                        vl.add(param);
                    }
                                        
                    ValueRow vr = new ValueRow(vl);                    
                    ins = new InsertStatement(table, new ElementList<Identifier>(names), vr);
                    
                    String q = ins.generate();
                    ps = connection.prepareStatement(q);
                    pa = new AssignmentVisitor(vaf, ps);
                    
    //                System.err.println("lineno: " + lineno);
    //                System.err.println("record: " + recno);
    //                System.err.println("query: " + q);
                }
                else {
                    pa.reset();
                    
                    for (int i = 0; i < line.length; i++) {
                        Column column = columnList.get(i);
                        VarcharHolder h = parse(column, line[i]);
                        VarcharParameter param = params[i];                                        
                        param.setValue(h);                    
                    }
                }
                            
                ins.traverse(null, pa);
                ps.addBatch();
            }
                
            int[] updateCounts = ps.executeBatch();            
            updated(updateCounts);            
            connection.commit();
            committed = true;
        }
        finally {
            if (!(committed)) {                
                QueryHelper.doRollback(connection);
            }            
        }
    }

    private VarcharHolder parse(Column column, String value) {
        if (value.equals("")) {
            DataType dataType = column.getDataType();        
            boolean textType = SQLTypeDefinition.isTextType(dataType.getDataType());
            
            if (!textType) {                
                value = null;
            }
        }
        
        return VarcharHolder.valueOf(value);
    }            
    
    
    public void updated(int[] updateCounts) {        
    }
}