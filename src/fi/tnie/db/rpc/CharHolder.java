/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;


import fi.tnie.db.types.CharType;
import fi.tnie.db.types.Type;

public class CharHolder
	extends StringHolder<CharType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2479889538730689743L;
	public static final CharHolder NULL_HOLDER = new CharHolder();
	public static final CharHolder EMPTY_HOLDER = new CharHolder("");
	
	/**
	 * TODO: should we have size?
	 */
		
	protected CharHolder() {
		super();
	}

	public CharHolder(String value) {
		super(value);
	}
	
	public static CharHolder valueOf(String s) {
		return 
			(s == null) ? NULL_HOLDER : 
			(s.equals("")) ? EMPTY_HOLDER :
			new CharHolder(s);
	}
	
	@Override
	public CharType getType() {
		return CharType.TYPE;
	}
	
	@Override
	public int getSqlType() {
		return Type.CHAR;
	}
}
