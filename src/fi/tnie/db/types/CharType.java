/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class CharType
	extends PrimitiveType<CharType> {

	public static final CharType TYPE = new CharType();
	
	private CharType() {
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.CHAR;
	}	
}
