/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.util;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import fi.tnie.db.exec.QueryProcessor;
import fi.tnie.db.query.QueryException;


public class SimpleQueryProcessor implements QueryProcessor {

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
	public void abort(Throwable e) {
		out.println("query failed: " + e.getMessage());		
	}

	@Override
	public void endQuery() {
		this.processedAt = System.currentTimeMillis();
		out.println("processing-time: " + getProcessingTime());
		out.println("rows processed: " + this.rows);
	}

	@Override
	public void startQuery(ResultSetMetaData m) 
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
	
	
	
}
