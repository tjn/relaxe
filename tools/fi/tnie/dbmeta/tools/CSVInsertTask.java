/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.dbmeta.tools;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;

import fi.tnie.db.ParameterAssignment;
import fi.tnie.db.QueryHelper;
import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.ElementList;
import fi.tnie.db.expr.InsertStatement;
import fi.tnie.db.expr.ValueParameter;
import fi.tnie.db.expr.ValueRow;
import fi.tnie.db.expr.ddl.SQLType;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ColumnMap;
import fi.tnie.db.meta.DataType;
import fi.tnie.db.meta.Table;

class CSVInsertTask
    extends CatalogTool {
        

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
            ColumnMap cm = table.columnMap();
            ElementList<ColumnName> names = new ElementList<ColumnName>();  
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
            int recno = 0;        
            
            ValueRow vr = new ValueRow();
            InsertStatement ins = new InsertStatement(table, names, vr);
            PreparedStatement ps = null;
            
            ValueParameter[] params = new ValueParameter[expectedColumnCount];
            ParameterAssignment pa = null;
                                    
            while ((line = p.getLine()) != null) {
                recno++;
                final int cols = line.length;
                int lineno = p.getLineNumber();
                
                if (cols != expectedColumnCount) {
                    throw new IllegalStateException("unexpected column count: " + cols + " at line " + lineno);
                }
                           
                if (ps == null) {                
                    for (int i = 0; i < params.length; i++) {                      
                        Column column = columnList.get(i);
                        Object value = parse(column, line[i]);                    
                        ValueParameter param = new ValueParameter(column, value);                    
                        params[i] = param;
                        vr.add(param);
                    }
                    
                    String q = ins.generate();
                    ps = connection.prepareStatement(q);
                    pa = new ParameterAssignment(ps);
                    
    //                System.err.println("lineno: " + lineno);
    //                System.err.println("record: " + recno);
    //                System.err.println("query: " + q);
                }
                else {
                    pa.reset();
                    
                    for (int i = 0; i < line.length; i++) {
                        Column column = columnList.get(i);
                        Object value = parse(column, line[i]);
                        ValueParameter param = params[i];                                        
                        param.setValue(value);                    
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

    private Object parse(Column column, String value) {
        if (value.equals("")) {
            DataType dataType = column.getDataType();        
            boolean textType = SQLType.isTextType(dataType.getDataType());
            
            if (!textType) {                
                value = null;
            }
        }
        
        return value;
    }            
    
    
    public void updated(int[] updateCounts) {        
    }
}