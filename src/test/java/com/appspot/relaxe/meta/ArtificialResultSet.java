/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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
package com.appspot.relaxe.meta;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtificialResultSet 
    implements ResultSet 
{
    private int position = -1;
    private boolean closed = false;
    
    private List<Column> columnList;    
    private int rowCount = 0;
        
    private class Column {
        private String label;
        private String name;
        private int type;
        private Class<?> javaType;
        
        protected Column(String label, String name, int type, Class<?> javaType) {
            super();
            this.label = label;
            this.name = name;
            this.type = type;
            this.javaType = javaType;
        }
        
        public String getLabel() {
            return label;
        }
        private String getName() {
            return name;
        }
        
        public int getType() {
            return type;
        }
        private Class<?> getJavaType() {
            return javaType;
        }
    }
        
    private class Key {
        private int row = 0;
        private int column = 0;
        
        private Key(int row, int column) {
            super();
            this.row = row;
            this.column = column;
        }
        
        @Override
        public boolean equals(Object obj) {
            Key k = (Key) obj;            
            return (this.row == k.row) && (this.column == k.column);
        }
    }
    
    private Map<Key, Object> data;
    
    
    @Override
    public boolean absolute(int arg0) throws SQLException {
        return throwNotImplementedBoolean();        
    }

    @Override
    public void afterLast() throws SQLException {
        this.position = this.rowCount;        
    }

    @Override
    public void beforeFirst() throws SQLException {
        this.position = -1;
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        throwNotImplemented();
    }

    @Override
    public void clearWarnings() throws SQLException {
        throwNotImplemented();

    }

    @Override
    public void close() throws SQLException {
        this.closed = true;
    }

    @Override
    public void deleteRow() throws SQLException {
        throwNotImplemented();
    }

    @Override
    public int findColumn(String n) throws SQLException {
        int x = 0;
        int index = 0;
        
        for (Column c : getColumnList()) {
            x++;
            String cn = c.getName();
            
            if (cn != null && cn.equals(n)) {
                index = x;
                break;
            }
        }
        
        if (index == 0) {
            throw new SQLException("No such column: " + n);
        }

        return index;
    }

    @Override
    public boolean first() throws SQLException {
        this.position = 0;
        return (this.position < this.rowCount);
    }

    @Override
    public Array getArray(int arg0) throws SQLException {
        throw notImplemented();
    }

    @Override
    public Array getArray(String arg0) throws SQLException {
        throw notImplemented();
    }

    @Override
    public InputStream getAsciiStream(int arg0) throws SQLException {
        throw notImplemented();
    }

    @Override
    public InputStream getAsciiStream(String arg0) throws SQLException {
        throw notImplemented();
    }

    @Override
    @Deprecated
    public BigDecimal getBigDecimal(int arg0) throws SQLException {
        throw notImplemented();
    }

    @Override
    @Deprecated
    public BigDecimal getBigDecimal(String arg0) throws SQLException {
        throw notImplemented();
    }

    @Override
    @Deprecated
    public BigDecimal getBigDecimal(int arg0, int arg1) throws SQLException {
        throw notImplemented();
    }

    @Override
    @Deprecated
    public BigDecimal getBigDecimal(String arg0, int arg1) throws SQLException {
        throw notImplemented();
    }

    @Override
    public InputStream getBinaryStream(int arg0) throws SQLException {
        throw notImplemented();
    }

    @Override
    public InputStream getBinaryStream(String arg0) throws SQLException {
        throw notImplemented();
    }

    @Override
    public Blob getBlob(int arg0) throws SQLException {
        throw notImplemented();
    }

    @Override
    public Blob getBlob(String arg0) throws SQLException {
        throw notImplemented();
    }

    @Override
    public boolean getBoolean(int arg0) throws SQLException {        
    	throw notImplemented();
    }

    @Override
    public boolean getBoolean(String arg0) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public byte getByte(int col) throws SQLException {
        Number n = getNumber(col);
        return (n == null) ? 0 : n.byteValue();
    }

    @Override
    public byte getByte(String cn) throws SQLException {        
        return getByte(findColumn(cn));
    }

    @Override
    public byte[] getBytes(int arg0) throws SQLException {
        throw notImplemented();
    }

    @Override
    public byte[] getBytes(String arg0) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public Reader getCharacterStream(int arg0) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public Reader getCharacterStream(String arg0) throws SQLException {
    	throw notImplemented();    
    }

    @Override
    public Clob getClob(int arg0) throws SQLException {
    	throw notImplemented();    
    }

    @Override
    public Clob getClob(String arg0) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public int getConcurrency() throws SQLException {
    	throw notImplemented();
    }

    @Override
    public String getCursorName() throws SQLException {
    	throw notImplemented();
    }

    @Override
    public Date getDate(int arg0) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public Date getDate(String arg0) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public Date getDate(int arg0, Calendar arg1) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public Date getDate(String arg0, Calendar arg1) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public double getDouble(int col) throws SQLException {
        Number n = getNumber(col);
        return (n == null) ? 0 : n.doubleValue();
    }

    @Override
    public double getDouble(String arg0) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public int getFetchDirection() throws SQLException {
    	return ResultSet.FETCH_FORWARD;
    }

    @Override
    public int getFetchSize() throws SQLException {
        return 0;
    }

    @Override
    public float getFloat(int col) throws SQLException {
        Number n = getNumber(col);
        return (n == null) ? 0 : n.floatValue();
    }

    @Override
    public float getFloat(String arg0) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public int getHoldability() throws SQLException {
        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
    }

    @Override
    public int getInt(int col) throws SQLException {
        Number n = getNumber(col);
        return (n == null) ? 0 : n.intValue();
    }

    @Override
    public int getInt(String col) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public long getLong(int col) throws SQLException {
        Number n = getNumber(col);
        return (n == null) ? 0 : n.longValue();
    }

    @Override
    public long getLong(String arg0) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        throw notImplemented();
    }

    @Override
    public Reader getNCharacterStream(int arg0) throws SQLException {
        throw notImplemented();
    }

    @Override
    public Reader getNCharacterStream(String arg0) throws SQLException {
        throw notImplemented();    
    }

    @Override
    public NClob getNClob(int arg0) throws SQLException {
        throw notImplemented();
    }

    @Override
    public NClob getNClob(String arg0) throws SQLException {
        throw notImplemented();
    }

    @Override
    public String getNString(int arg0) throws SQLException {
        throw notImplemented();
    }

    @Override
    public String getNString(String arg0) throws SQLException {
        throw notImplemented();
    }

    @Override
    public Object getObject(int col) throws SQLException {        
        return get(col);
    }

    @Override
    public Object getObject(String cn) throws SQLException {        
        return get(findColumn(cn));
    }

    @Override
    public Object getObject(int col, Map<String, Class<?>> arg1)
            throws SQLException {        
        throw notImplemented();
    }

    @Override
    public Object getObject(String cn, Map<String, Class<?>> typeMap)
            throws SQLException {
        return getObject(findColumn(cn), typeMap);
    }

    @Override
    public Ref getRef(int arg0) throws SQLException {
        throw notImplemented();
    }

    @Override
    public Ref getRef(String arg0) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public int getRow() throws SQLException {
        return (this.position < 0 || this.position >= this.rowCount) ? 0 : this.position + 1;
    }

    @Override
    public RowId getRowId(int arg0) throws SQLException {        
        throw notImplemented();
    }

    @Override
    public RowId getRowId(String arg0) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public SQLXML getSQLXML(int arg0) throws SQLException {
        throw notImplemented();
    }

    @Override
    public SQLXML getSQLXML(String arg0) throws SQLException {
        throw notImplemented();
    }

    @Override
    public short getShort(int col) throws SQLException {
        Number n = getNumber(col);
        return (n == null) ? 0 : n.shortValue();
    }

    @Override
    public short getShort(String arg0) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Statement getStatement() throws SQLException {
        throw notImplemented();
    }

    @Override
    public String getString(int col) throws SQLException {
        Object o = get(col);
        return (o == null) ? null : o.toString();
    }

    @Override
    public String getString(String arg0) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Time getTime(int arg0) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public Time getTime(String arg0) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public Time getTime(int arg0, Calendar arg1) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public Time getTime(String arg0, Calendar arg1) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public Timestamp getTimestamp(int arg0) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public Timestamp getTimestamp(String arg0) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public Timestamp getTimestamp(int arg0, Calendar arg1) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public Timestamp getTimestamp(String arg0, Calendar arg1)
            throws SQLException {
    	throw notImplemented();
    }

    @Override
    public int getType() throws SQLException {
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    @Override
    public URL getURL(int arg0) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public URL getURL(String arg0) throws SQLException {
    	throw notImplemented();
    }

    @Override
    @Deprecated
    public InputStream getUnicodeStream(int arg0) throws SQLException {
    	throw notImplemented();
    }

    @Override
    @Deprecated
    public InputStream getUnicodeStream(String arg0) throws SQLException {
    	throw notImplemented();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
    	throw notImplemented();
    }

    @Override
    public void insertRow() throws SQLException {
    	throw notImplemented();
    }

    @Override
    public boolean isAfterLast() throws SQLException {
    	return position >= this.rowCount; 
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        return position < 0;
    }

    @Override
    public boolean isClosed() throws SQLException {        
        return this.closed;
    }

    @Override
    public boolean isFirst() throws SQLException {
        return this.position == 0;
    }

    @Override
    public boolean isLast() throws SQLException {
    	return this.position == (this.rowCount - 1);
    }

    @Override
    public boolean last() throws SQLException {
    	this.position = (this.rowCount - 1);
    	return true;
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void moveToInsertRow() throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean next() throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean previous() throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void refreshRow() throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean relative(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean rowInserted() throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setFetchDirection(int arg0) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setFetchSize(int arg0) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateArray(int arg0, Array arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateArray(String arg0, Array arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateAsciiStream(int arg0, InputStream arg1)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateAsciiStream(String arg0, InputStream arg1)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateAsciiStream(int arg0, InputStream arg1, int arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateAsciiStream(String arg0, InputStream arg1, int arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateAsciiStream(int arg0, InputStream arg1, long arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateAsciiStream(String arg0, InputStream arg1, long arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateBigDecimal(int arg0, BigDecimal arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateBigDecimal(String arg0, BigDecimal arg1)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateBinaryStream(int arg0, InputStream arg1)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateBinaryStream(String arg0, InputStream arg1)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateBinaryStream(int arg0, InputStream arg1, int arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateBinaryStream(String arg0, InputStream arg1, int arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateBinaryStream(int arg0, InputStream arg1, long arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateBinaryStream(String arg0, InputStream arg1, long arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateBlob(int arg0, Blob arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateBlob(String arg0, Blob arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateBlob(int arg0, InputStream arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateBlob(String arg0, InputStream arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateBlob(int arg0, InputStream arg1, long arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateBlob(String arg0, InputStream arg1, long arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateBoolean(int arg0, boolean arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateBoolean(String arg0, boolean arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateByte(int arg0, byte arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateByte(String arg0, byte arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateBytes(int arg0, byte[] arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateBytes(String arg0, byte[] arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateCharacterStream(int arg0, Reader arg1)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateCharacterStream(String arg0, Reader arg1)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateCharacterStream(int arg0, Reader arg1, int arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateCharacterStream(String arg0, Reader arg1, int arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateCharacterStream(int arg0, Reader arg1, long arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateCharacterStream(String arg0, Reader arg1, long arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateClob(int arg0, Clob arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateClob(String arg0, Clob arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateClob(int arg0, Reader arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateClob(String arg0, Reader arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateClob(int arg0, Reader arg1, long arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateClob(String arg0, Reader arg1, long arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateDate(int arg0, Date arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateDate(String arg0, Date arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateDouble(int arg0, double arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateDouble(String arg0, double arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateFloat(int arg0, float arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateFloat(String arg0, float arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateInt(int arg0, int arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateInt(String arg0, int arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateLong(int arg0, long arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateLong(String arg0, long arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateNCharacterStream(int arg0, Reader arg1)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateNCharacterStream(String arg0, Reader arg1)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateNCharacterStream(int arg0, Reader arg1, long arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateNCharacterStream(String arg0, Reader arg1, long arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateNClob(int arg0, NClob arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateNClob(String arg0, NClob arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateNClob(int arg0, Reader arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateNClob(String arg0, Reader arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateNClob(int arg0, Reader arg1, long arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateNClob(String arg0, Reader arg1, long arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateNString(int arg0, String arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateNString(String arg0, String arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateNull(int arg0) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateNull(String arg0) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateObject(int arg0, Object arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateObject(String arg0, Object arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateObject(int arg0, Object arg1, int arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateObject(String arg0, Object arg1, int arg2)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateRef(int arg0, Ref arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateRef(String arg0, Ref arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateRow() throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateRowId(int arg0, RowId arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateRowId(String arg0, RowId arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateSQLXML(int arg0, SQLXML arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateSQLXML(String arg0, SQLXML arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateShort(int arg0, short arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateShort(String arg0, short arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateString(int arg0, String arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateString(String arg0, String arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateTime(int arg0, Time arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateTime(String arg0, Time arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateTimestamp(int arg0, Timestamp arg1) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateTimestamp(String arg0, Timestamp arg1)
            throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean wasNull() throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        return null;
    }
    
    public void setColumnType(int column, Class<?> type) {
        
    }
    
    public Class<?> getColumnType(int column) throws SQLException {
        return getColumn(column).getJavaType();
    }
    
    public void set(int row, int column, Object value) {
        
    }
    
    private boolean throwNotImplementedBoolean() 
        throws SQLException {
        throw notImplemented();
    }
    
    private void throwNotImplemented() throws SQLException {
        throw notImplemented();
    }    
    
    private boolean throwNotImplementedInt() 
        throws SQLException {
        throw notImplemented();
    }
    
    
    private SQLException notImplemented() {
        return new SQLException("not implemented");
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getRowCount() {
        return rowCount;
    }
    
    
    private List<Column> getColumnList() {
        if (columnList == null) {
            columnList = new ArrayList<Column>();           
        }

        return columnList;
    }
        
    public void addColumn(String name, int type, Class<?> klass) {
        getColumnList().add(new Column(name, name, type, klass));
    }    
    
    private Object get(int col) 
        throws SQLException {        
        validateKey(this.position, col);
        return getData().get(new Key(this.position, col));
    }   
    
    
    private void validateKey(int row, int col) 
        throws SQLException {
        if (row < 0) {
            throw new SQLException("position is before the first row");
        }
        if (row >= this.rowCount) {
            throw new SQLException("position is after the last row");
        }
        
        if (col < 1 || col > getColumnList().size()) {
            throw new SQLException("column-index: " + col);
        }        
    }

    private Map<Key, Object> getData() {
        if (this.data == null) {
            this.data = new HashMap<Key, Object>();
        }

        return this.data;
    }
    
    private Column getColumn(int col) 
        throws SQLException {
        if (col < 1 || col > getColumnList().size()) {
            throw new SQLException("column-index: " + col);
        }
        
        return getColumnList().get(col - 1);
    }    
    
    private Number getNumber(int col) 
        throws SQLException {
               
        try {       
             
            return (Number) get(col);
        }
        catch (ClassCastException e) {
            throw new SQLException(e.getMessage(), e);
        }        
    }

	@Override
	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getObject(String columnLabel, Class<T> type)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
