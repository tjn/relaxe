/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.types;

public abstract class ArrayType<T extends ArrayType<T, E>, E extends PrimitiveType<E>>
	extends AbstractPrimitiveType<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5821839514528542759L;

	protected ArrayType() {		
	}
	
	public abstract E getElementType();
	
	
	@Override
	public final int getSqlType() {
		return PrimitiveType.ARRAY;
	}

	
	@Override
	public ArrayType<? extends T, ?> asArrayType() {
		return self();
	}
}
