/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.OtherType;
import fi.tnie.db.types.PrimitiveType;

public abstract class OtherAssignment<O extends OtherType<O>, V extends Serializable, T extends PrimitiveType<T>, H extends PrimitiveHolder<V, T, H>>
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