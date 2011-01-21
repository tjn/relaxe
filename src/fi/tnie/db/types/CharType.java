/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.rpc.PrimitiveHolder;

public class CharType
	extends PrimitiveType<CharType> {

	public static final CharType TYPE = new CharType();
	
	private CharType() {
	}
	
	@Override
	public int getSqlType() {
		return Type.CHAR;
	}
	
	@Override
	public PrimitiveHolder<?, CharType> nil() {
		return CharHolder.NULL_HOLDER;
	}	
}
