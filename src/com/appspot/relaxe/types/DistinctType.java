/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.types;

public abstract class DistinctType<T extends DistinctType<T>>
	extends AbstractPrimitiveType<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5821839514528542759L;

	protected DistinctType() {		
	}
	
	/**
	 * The name of the distinct type.
	 * 
	 * @return
	 */
	public abstract String getName();
	
	@Override
	public final int getSqlType() {
		return AbstractPrimitiveType.DISTINCT;
	}
}
