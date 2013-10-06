/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.pg.expr;

import com.appspot.relaxe.expr.SimpleTypeName;
import com.appspot.relaxe.expr.Type;
import com.appspot.relaxe.expr.ddl.AbstractCharacterType;

public class PGText
	extends AbstractCharacterType {
	
		/**
	 * 
	 */
	private static final long serialVersionUID = 867305622555760454L;
	
	private static final Type NAME = new SimpleTypeName("text");


	@Override
	public Type getSQLTypeName() {
		return NAME;
	}
}
