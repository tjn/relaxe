/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ValueExtractor
	implements Extractor {
	
	private int column;
	private Object last = null;

	public ValueExtractor(int column) {
		super();			
		this.column = column;
	}
	
	public int getColumn() {
		return column;
	}

	public Object last() {
		return this.last;
	}

	public abstract Object doExtract(ResultSet rs)
		throws SQLException;
	
	public void extract(ResultSet rs)
		throws SQLException {
		this.last = doExtract(rs);
		set(this.last);			
	}
		
	protected void set(Object value) {	
	}
}