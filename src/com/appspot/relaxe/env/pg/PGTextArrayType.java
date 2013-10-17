/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.pg;

import com.appspot.relaxe.types.ArrayType;
import com.appspot.relaxe.types.VarcharType;

public class PGTextArrayType
	extends ArrayType<PGTextArrayType, VarcharType> {

	
	public static final PGTextArrayType TYPE = new PGTextArrayType();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7152862866657378339L;

	@Override
	public VarcharType getElementType() {
		return VarcharType.TYPE;
	}
	
	@Override
	public String getName() {
		return "_text";
	}

	@Override
	public PGTextArrayType self() {
		return this;
	}
	
	@Override
	public ArrayType<? extends PGTextArrayType, ?> asArrayType() {
		return this;
	}
}
