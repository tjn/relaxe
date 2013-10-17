/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;


public final class IntTypeDefinition
	extends AbstractIntegalNumberDefinition {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6177212680749147403L;
	
	
	public static final IntTypeDefinition DEFINITION = new IntTypeDefinition();
	

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected IntTypeDefinition() {
	}


	public IntTypeDefinition(Integer precision) {
		super(precision);	
	}

	public IntTypeDefinition(int precision) {
		super(Integer.valueOf(precision));
	}

	public static IntTypeDefinition get(Integer precision) {		
		return (precision == null) ? DEFINITION : new IntTypeDefinition(precision);
	}

	@Override
	public SQLTypeDefinition.Name getSQLTypeName() {
		return SQLTypeDefinition.Name.INTEGER;
	}
}
