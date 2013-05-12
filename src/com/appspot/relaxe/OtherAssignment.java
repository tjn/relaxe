/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.appspot.relaxe.rpc.AbstractPrimitiveHolder;
import com.appspot.relaxe.types.OtherType;
import com.appspot.relaxe.types.AbstractPrimitiveType;


public abstract class OtherAssignment<O extends OtherType<O>, V extends Serializable, T extends AbstractPrimitiveType<T>, H extends AbstractPrimitiveHolder<V, T, H>>
	extends AbstractParameterAssignment<V, T, H> {
		
	public OtherAssignment(H value) {
		super(value);
	}

	@Override
	public void assign(PreparedStatement ps, int ordinal, V newValue) throws SQLException {
		Object o = getObject(newValue);		
		int t = getType().getSqlType();
		ps.setObject(ordinal, o, t);
	}
	
	protected Object getObject(V v) {
		return v;		
	}
	
	protected abstract O getType();
}
