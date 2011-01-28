/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import java.io.Serializable;

import fi.tnie.db.types.PrimitiveType;


public abstract class PrimitiveHolder<V extends Serializable, T extends PrimitiveType<T>>
	extends Holder<V, T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2373967913129102220L;

	public abstract int getSqlType();
	
	@Override
	public String toString() {
		return super.toString() + "[" + getSqlType() + "]: " + this.value();
	}
}
