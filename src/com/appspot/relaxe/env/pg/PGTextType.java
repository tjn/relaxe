/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.pg;

import com.appspot.relaxe.types.AbstractPrimitiveType;
import com.appspot.relaxe.types.PrimitiveType;

public class PGTextType
	extends AbstractPrimitiveType<PGTextType> {
	
	public static final PGTextType TYPE = new PGTextType();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7152862866657378339L;

	
	@Override
	public String getName() {
		return "text";
	}

	@Override
	public PGTextType self() {
		return this;
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.VARCHAR;
	}
}
