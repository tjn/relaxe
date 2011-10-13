/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class CharType
	extends PrimitiveType<CharType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 619972121801208648L;
	public static final CharType TYPE = new CharType();
	
	private CharType() {
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.CHAR;
	}	
}
