/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.types;

public class CharType
	extends AbstractPrimitiveType<CharType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 619972121801208648L;
	public static final CharType TYPE = new CharType();
	
	private CharType() {
	}
	
	@Override
	public int getSqlType() {
		return AbstractPrimitiveType.CHAR;
	}

	@Override
	public CharType self() {
		return this;
	}	
}
