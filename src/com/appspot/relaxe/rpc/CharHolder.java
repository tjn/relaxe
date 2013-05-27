/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;


import com.appspot.relaxe.types.CharType;
import com.appspot.relaxe.types.AbstractPrimitiveType;
import com.appspot.relaxe.types.PrimitiveType;

public class CharHolder
	extends StringHolder<CharType, CharHolder> {

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
		return PrimitiveType.CHAR;
	}
	
	@Override
	public CharHolder asCharHolder() {
		return this;
	}

	@Override
	public CharHolder self() {
		return this;
	}

	public static CharHolder of(PrimitiveHolder<?, ?, ?> holder) {
		return holder.asCharHolder();
	}
	
}
