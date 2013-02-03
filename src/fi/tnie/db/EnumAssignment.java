/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.EnumType;
import fi.tnie.db.types.Enumerable;

public abstract class EnumAssignment<V extends Enum<V> & Enumerable, T extends EnumType<T, V>, H extends PrimitiveHolder<V, T, H>>
	extends AbstractParameterAssignment<V, T, H> {
		
	public EnumAssignment(H value) {
		super(value);
	}

	@Override
	public void assign(PreparedStatement ps, int ordinal, V newValue) throws SQLException {
		Object o = getObject(newValue);		
				
		int t = getType().getSqlType();
		ps.setObject(ordinal, o, t);				
	}
	
	protected Object getObject(V v) {
		return v.value();		
	}
	
	protected abstract T getType();
}
