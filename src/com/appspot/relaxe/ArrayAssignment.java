/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.io.Serializable;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.appspot.relaxe.rpc.ArrayHolder;
import com.appspot.relaxe.rpc.ArrayValue;
import com.appspot.relaxe.types.ArrayType;
import com.appspot.relaxe.types.PrimitiveType;


public abstract class ArrayAssignment<
	V extends Serializable, 
	E extends PrimitiveType<E>, 
	A extends ArrayValue<V>,
	T extends ArrayType<T, E>, 
	H extends ArrayHolder<V, A, E, T, H>
>
	extends AbstractParameterAssignment<A, T, H> {
		
	public ArrayAssignment(H value) {
		super(value);
	}
	
	@Override
	public void assign(PreparedStatement ps, int ordinal, A v)
		throws SQLException {
		
		Connection c = ps.getConnection();
		
		V[] elems = (v == null) ? null : v.toArray();
		
		Array array = (elems == null) ? null : createArrayOf(c, elems); 
		
		ps.setArray(ordinal, array);
		
		if (array != null) {
			close(array);
		}
		
	}

	protected Array createArrayOf(Connection c, V[] elems) throws SQLException {
		String typeName = getType().getName();
		Array a = c.createArrayOf(typeName, elems);
		return a;
	}

//	@Override
//	public void assign(PreparedStatement ps, int ordinal, V newValue) throws SQLException {
//		Connection c = ps.getConnection();
//		
//		
//		
//		c.createArrayOf("", arg1);
//		
//		
//		Object o = getObject(newValue);		
//				
//		int t = getType().getSqlType();
//		ps.setObject(ordinal, o, t);				
//	}
//	
//	protected Object getObject(V v) {
//		return v.value();		
//	}
	
	protected void close(Array array)
		throws SQLException {
		array.free();
	}

	protected abstract T getType();
}
