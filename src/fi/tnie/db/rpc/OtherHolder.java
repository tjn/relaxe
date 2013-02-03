/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;


import java.io.Serializable;

import fi.tnie.db.types.OtherType;
import fi.tnie.db.types.PrimitiveType;

public abstract class OtherHolder<V extends Serializable, T extends OtherType<T>, H extends OtherHolder<V, T, H>>
	extends PrimitiveHolder<V, T, H> {

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
	public abstract T getType();
	
	@Override
	public final int getSqlType() {
		return PrimitiveType.OTHER;
	}

	
	@Override
	public OtherHolder<?, ?, ?> asOtherHolder(String typeName) {
		return getType().getName().equals(typeName) ? self() : null;
	}
	
	@Override
	public abstract H self();
}
