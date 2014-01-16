/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe;

import java.io.Serializable;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.appspot.relaxe.types.ArrayType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.ArrayHolder;
import com.appspot.relaxe.value.ArrayValue;


public abstract class ArrayAssignment<
	V extends Serializable, 
	E extends ValueType<E>, 
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
