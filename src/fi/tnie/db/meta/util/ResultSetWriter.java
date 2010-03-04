/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import fi.tnie.db.QueryException;

public class ResultSetWriter 
	extends AbstractQueryProcessor {
	
	private PrintWriter out;
	private boolean closeAtEnd;
	private String delimiter = "\t";
		
	protected ResultSetWriter(File f) 
		throws FileNotFoundException {
		this(new FileOutputStream(f), true);
	}
	
	public ResultSetWriter(OutputStream os, boolean close) {
		super();
		out = new PrintWriter(new OutputStreamWriter(os));
		this.closeAtEnd = close;
	}	
	
	@Override
	public void startQuery(ResultSetMetaData m) throws SQLException {
		int cc = m.getColumnCount();
		
		StringBuffer buf = new StringBuffer();
			 		
		for (int i = 1; i <= cc; i++) {
			buf.append(m.getColumnLabel(i));
			
			if (i < cc) {
				buf.append(delimiter);
			}						
		}
		
		out.println(buf.toString());
	}
	
	
		
	@Override
	public void endQuery() throws QueryException {		
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
		out.println("-----------------------------------------");
		out.print("-- ");
		out.println(s);
		out.println("-----------------------------------------");
	}
	
	
}
