/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;


import java.io.Serializable;

import fi.tnie.db.types.OtherType;
import fi.tnie.db.types.PrimitiveType;

public abstract class OtherHolder<V extends Serializable, T extends OtherType<T>>
	extends PrimitiveHolder<V, T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2479889538730689743L;
		
	/**
	 * TODO: should we have size?
	 */
		
	protected OtherHolder() {
		super();
	}

	@Override
	public T getType() {
		// TODO:
		return null; // OtherType.TYPE;
	}
	
	@Override
	public final int getSqlType() {
		return PrimitiveType.OTHER;
	}
}
