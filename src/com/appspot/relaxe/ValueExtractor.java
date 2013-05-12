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

import com.appspot.relaxe.rpc.AbstractPrimitiveHolder;
import com.appspot.relaxe.types.AbstractPrimitiveType;


public abstract class ValueExtractor
	<V extends Serializable, T extends AbstractPrimitiveType<T>, H extends AbstractPrimitiveHolder<V, T, H>> {
	
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