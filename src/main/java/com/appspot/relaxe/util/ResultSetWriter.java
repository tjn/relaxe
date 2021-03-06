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
package com.appspot.relaxe.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import com.appspot.relaxe.query.QueryException;


public class ResultSetWriter 
	extends AbstractQueryProcessor {
	
	private PrintWriter out;
	private boolean closeAtEnd;
	private String delimiter = "\t";
	private long processed = 0;
		
	protected ResultSetWriter(File f) 
		throws FileNotFoundException {
		this(new FileOutputStream(f), true);
	}
	
	/**
	 * 
	 * @param os
	 * @param close Whether or not os should be close when the query is processed
	 */
	
	public ResultSetWriter(OutputStream os, boolean close) {
		super();
		out = new PrintWriter(new OutputStreamWriter(os));
		this.closeAtEnd = close;
	}	
	
	@Override
	public void startResultSet(ResultSetMetaData m) throws SQLException {
		int cc = m.getColumnCount();
		
		StringBuffer buf = new StringBuffer();
			 		
		for (int i = 1; i <= cc; i++) {
			buf.append(m.getColumnLabel(i));
			
			if (i < cc) {
				buf.append(delimiter);
			}						
		}
		
		out.println(buf.toString());
		buf.setLength(0);
		
		
		for (int i = 1; i <= cc; i++) {
			buf.append(m.getColumnName(i));
			
			if (i < cc) {
				buf.append(delimiter);
			}						
		}
		
		out.println(buf.toString());
		buf.setLength(0);
				
		for (int i = 1; i <= cc; i++) {
            buf.append(m.getColumnTypeName(i));
            
            if (i < cc) {
                buf.append(delimiter);
            }                       
        }
		
		out.println(buf.toString());
	    buf.setLength(0);
	    
		for (int i = 1; i <= cc; i++) {
			int type = m.getColumnType(i);
            buf.append(type + "=" + name(type));
            
            if (i < cc) {
                buf.append(delimiter);
            }                       
        }
		
		out.println(buf.toString());
	    buf.setLength(0);	    
	}
	
	
	private String name(int type) {		
		String n = "";
		
		switch (type) {
		case Types.BIT:
			n = "BIT";
			break;
		case Types.TINYINT:
				n = "TINYINT";
				break;
		case Types.SMALLINT:
				n = "SMALLINT";
				break;
		case Types.INTEGER:
				n = "INTEGER";
				break;
		case Types.BIGINT:
				n = "BIGINT";
				break;
		case Types.FLOAT:
				n = "FLOAT";
				break;
		case Types.REAL:
				n = "REAL";
				break;
		case Types.DOUBLE:
				n = "DOUBLE";
				break;
		case Types.NUMERIC:
				n = "NUMERIC";
				break;
		case Types.DECIMAL:
				n = "DECIMAL";
				break;
		case Types.CHAR:
				n = "CHAR";
				break;
		case Types.VARCHAR:
				n = "VARCHAR";
				break;
		case Types.LONGVARCHAR:
				n = "LONGVARCHAR";
				break;
		case Types.DATE:
				n = "DATE";
				break;
		case Types.TIME:
				n = "TIME";
				break;
		case Types.TIMESTAMP:
				n = "TIMESTAMP";
				break;
		case Types.BINARY:
				n = "BINARY";
				break;
		case Types.VARBINARY:
				n = "VARBINARY";
				break;
		case Types.LONGVARBINARY:
				n = "LONGVARBINARY";
				break;
		case Types.NULL:
				n = "NULL";
				break;
		case Types.OTHER:
				n = "OTHER";
				break;
		case Types.JAVA_OBJECT:
				n = "JAVA_OBJECT";
				break;
		case Types.DISTINCT:
				n = "DISTINCT";
				break;
		case Types.STRUCT:
				n = "STRUCT";
				break;
		case Types.ARRAY:
				n = "ARRAY";
				break;
		case Types.BLOB:
				n = "BLOB";
				break;
		case Types.CLOB:
				n = "CLOB";
				break;
		case Types.REF:
				n = "REF";
				break;
		case Types.DATALINK:
				n = "DATALINK";
				break;
		case Types.BOOLEAN:
				n = "BOOLEAN";
				break;
		case Types.ROWID:
				n = "ROWID";
				break;
		case Types.NCHAR:
				n = "NCHAR";
				break;
		case Types.NVARCHAR:
				n = "NVARCHAR";
				break;
		case Types.LONGNVARCHAR:
				n = "LONGNVARCHAR";
				break;
		case Types.NCLOB:
				n = "NCLOB";
				break;
		case Types.SQLXML:
				n = "SQLXML";
				break;
		default:
			break;
		}
		
		return n;
	}
	
	
	@Override
	public void updated(int updateCount) {
		out.print("updated: ");
		out.println(updateCount);		
	}
	
		
	@Override
	public void endResultSet() throws QueryException {		
		if (closeAtEnd) {
			this.out.close();
			this.out = null;
		}
		else {
			this.out.flush();			
		}		
	}

	@Override
	public void process(ResultSet rs, long ordinal) throws SQLException {
		int cc = rs.getMetaData().getColumnCount();
		
		StringBuffer buf = new StringBuffer();
			 		
		for (int i = 1; i <= cc; i++) {
			buf.append(rs.getObject(i));
			
			if (i < cc) {
				buf.append(delimiter);
			}						
		}
		
		out.println(buf.toString());
		processed++;
	}
	
	
	public long processed() {
		return processed;
	}
	
	@Override
	public void prepare() {
		if (this.out == null) {
			throw new NullPointerException("no output stream available");
		}
	}
	
	
	public void println(String s) {
		this.out.println(s);
	}
	
	public void header(String s) {
//		out.println("-----------------------------------------");
//		out.print("\"-- ");
		out.println("\"" + s + "\"");
//		out.println("-----------------------------------------");
	}

	
}
