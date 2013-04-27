/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.types;

public abstract class ArrayType<T extends ArrayType<T, E>, E extends PrimitiveType<E>>
	extends PrimitiveType<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5821839514528542759L;

	protected ArrayType() {		
	}
	
	public abstract E getElementType();
	
	/**
	 * The name of the distinct type.
	 * 
	 * @return
	 */
	public abstract String getName();
	
	@Override
	public final int getSqlType() {
		return PrimitiveType.ARRAY;
	}
}
