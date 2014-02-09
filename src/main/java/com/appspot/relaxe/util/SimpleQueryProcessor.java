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

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.appspot.relaxe.exec.QueryProcessor;
import com.appspot.relaxe.exec.UpdateProcessor;
import com.appspot.relaxe.query.QueryException;



public class SimpleQueryProcessor 
	implements QueryProcessor, UpdateProcessor {

	private long preparedAt;
	private long startedAt;
	private long processedAt;
	private PrintWriter out;
	private String statement;
	private int rows;
	private String columnDelimiter;
			
	public SimpleQueryProcessor(PrintWriter out) {
		this(out, ";");
	}
	
	
	public SimpleQueryProcessor(PrintWriter out, String cd) {
		super();
		if (out == null) {
			throw new NullPointerException();
		}
		this.out = out;
		this.columnDelimiter = (cd == null) ? "" : cd;
	}

	public long getExecutionTime() {
		return startedAt - preparedAt;		
	}

	protected long getPreparedAt() {
		return preparedAt;
	}

	protected void setPreparedAt(long preparedAt) {
		this.preparedAt = preparedAt;
	}

	protected PrintWriter getOut() {
		return out;
	}

	public long getProcessingTime() {
		return processedAt - startedAt;
	}

	@Override
	public void finish() {
		out.flush();		
	}

	@Override
	public void prepare() {
		this.preparedAt = System.currentTimeMillis();
	}

	@Override
	public void process(ResultSet rs, long ordinal) throws QueryException, SQLException {
		this.rows++;
						
		StringBuffer buf = new StringBuffer();
		ResultSetMetaData m = rs.getMetaData();
		int cc = m.getColumnCount();
		
		for (int i = 1; i <= cc; i++) {
			buf.append(rs.getString(i));
			
			if (i < cc) {
				buf.append(this.columnDelimiter);
			}
		}
		
		out.println(buf.toString());		
	}

	@Override
	public void abort(Exception e) {
		out.println("query failed: " + e.getMessage());		
	}

	@Override
	public void endResultSet() {
		this.processedAt = System.currentTimeMillis();
		out.println("processing-time: " + getProcessingTime());
		out.println("rows processed: " + this.rows);
	}

	@Override
	public void startResultSet(ResultSetMetaData m) 
		throws SQLException {
				
		startedAt = System.currentTimeMillis();
		
		this.rows = 0;
						
		out.println("query: " + statement);
		out.println("execution time: " + getExecutionTime());
		
		StringBuffer buf = new StringBuffer();
		int cc = m.getColumnCount();
		
		for (int i = 1; i <= cc; i++) {
			buf.append(m.getColumnLabel(i));
			
			if (i < cc) {
				buf.append(this.columnDelimiter);
			}
		}
		out.println(buf.toString());
	}

	@Override
	public void updated(int updateCount) {
		out.println("row(s) affected: " + updateCount);
	}
	

	public static long process(ResultSet rs, QueryProcessor qp) 
	   throws Exception {

	    qp.prepare();
	         
	    long ordinal = 0;
	    
	    try {        
	        qp.startResultSet(rs.getMetaData());
	                
	        while (rs.next()) {
	            ordinal++;
	            qp.process(rs, ordinal);
	        }
	                
	        qp.endResultSet();                  
	    }
	    catch (Exception e) {
//	        logger().error(e.getMessage(), e);
	        qp.abort(e);
	    }
	    finally {
	        qp.finish();
	    }
	    
	    return ordinal;
	}
	
}

