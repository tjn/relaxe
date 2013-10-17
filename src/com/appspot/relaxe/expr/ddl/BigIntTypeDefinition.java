/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl;

import com.appspot.relaxe.expr.ddl.types.AbstractIntegalNumberDefinition;
import com.appspot.relaxe.expr.ddl.types.SQLTypeDefinition;

public final class BigIntTypeDefinition
	extends AbstractIntegalNumberDefinition {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5161923139604901278L;
	
	public static final BigIntTypeDefinition DEFINITION = new BigIntTypeDefinition();
	

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected BigIntTypeDefinition() {
	}


	public BigIntTypeDefinition(Integer precision) {
		super(precision);	
	}

	public BigIntTypeDefinition(int precision) {
		super(Integer.valueOf(precision));
	}

	public static BigIntTypeDefinition get(Integer precision) {		
		return (precision == null) ? DEFINITION : new BigIntTypeDefinition(precision);
	}

	@Override
	public SQLTypeDefinition.Name getSQLTypeName() {
		return SQLTypeDefinition.Name.BIGINT;
	}
}
