/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.PrimitiveType;


public abstract class ValueExtractor
	<V extends Serializable, T extends PrimitiveType<T>, H extends PrimitiveHolder<V, T, H>> {
	
	private int column;

	public ValueExtractor(int column) {
		super();			
		this.column = column;
	}
	
	public int getColumn() {
		return column;
	}

	public abstract H extractValue(ResultSet rs)
		throws SQLException;	
}