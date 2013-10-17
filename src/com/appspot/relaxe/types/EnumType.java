/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.types;

public abstract class EnumType<T extends EnumType<T, E>, E extends Enum<E> & Enumerable>
	extends OtherType<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5821839514528542759L;

	protected EnumType() {		
	}
		
	public abstract Class<E> getValueType();	
}
