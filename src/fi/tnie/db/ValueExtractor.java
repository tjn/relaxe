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
	private Holder last = null;

	public ValueExtractor(int column) {
		super();			
		this.column = column;
	}
	
	public int getColumn() {
		return column;
	}

	public Holder last() {
		return this.last;
	}

	public abstract Holder extractValue(ResultSet rs)
		throws SQLException;
	
	public void extract(ResultSet rs)
		throws SQLException {
		this.last = extractValue(rs);
		set(this.last);
	}
		
	protected void set(Object value) {	
	}
	
	
	
}