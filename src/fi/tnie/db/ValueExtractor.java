/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.rpc.Holder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;

public abstract class ValueExtractor
	<V extends Serializable, T extends PrimitiveType<T>>
	implements Extractor {
	
	private int column;
	private PrimitiveHolder<V, T> last = null;

	public ValueExtractor(int column) {
		super();			
		this.column = column;
	}
	
	public int getColumn() {
		return column;
	}

	public Holder<V, T> last() {
		return this.last;
	}

	public abstract PrimitiveHolder<V, T> extractValue(ResultSet rs)
		throws SQLException;
	
	public void extract(ResultSet rs)
		throws SQLException {
		this.last = extractValue(rs);
		set(this.last);
	}
		
	protected void set(PrimitiveHolder<V, T> value) {	
	}	
}