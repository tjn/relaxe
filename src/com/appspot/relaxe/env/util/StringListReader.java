/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class StringListReader extends AbstractQueryProcessor {
		
	private Collection<String> destination;
	private int column;
	
	private int rowCount = 0; 
	
	public StringListReader(Collection<String> dest) {
		this(dest, 1);
	}
	
	public StringListReader(Collection<String> dest, int column) {
		super();
		
		if (dest == null) {
			throw new NullPointerException("'dest' must not be null");
		}
		
		this.destination = dest;
		this.column = column;
	}
	
	@Override
	public void prepare() {
	    rowCount = 0;	
	}
	

	@Override
	public void process(ResultSet rs, long ordinal) throws SQLException {
		String value = rs.getString(column);
		this.destination.add(value);
		rowCount++;
	}
	
	public int getRowCount() {
        return rowCount;
    }
}
