/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;


public abstract class AbstractParameterAssignment<V extends Serializable, T extends PrimitiveType<T>, H extends PrimitiveHolder<V, T>>
	implements ParameterAssignment {	
	private H holder;
	
	public AbstractParameterAssignment(H value) {
		super();
		this.holder = value;
	}

	public void assign(PreparedStatement ps, int ordinal) 
		throws SQLException {
		
		V v = holder.value();
		
		if (v == null) {
			ps.setNull(ordinal, holder.getSqlType());
		}
		else {
			assign(ps, ordinal, v);
		}
	}
	
	public abstract void assign(PreparedStatement ps, int ordinal, V newValue)
		throws SQLException;
}
