/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;


public final class TinyIntTypeDefinition
	extends AbstractIntegalNumberDefinition {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4142797245840237139L;
	public static final TinyIntTypeDefinition DEFINITION = new TinyIntTypeDefinition();
	

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected TinyIntTypeDefinition() {
	}


	public TinyIntTypeDefinition(Integer precision) {
		super(precision);	
	}

	public TinyIntTypeDefinition(int precision) {
		super(Integer.valueOf(precision));
	}

	public static TinyIntTypeDefinition get(Integer precision) {		
		return (precision == null) ? DEFINITION : new TinyIntTypeDefinition(precision);
	}

	@Override
	public SQLTypeDefinition.Name getSQLTypeName() {
		return SQLTypeDefinition.Name.TINYINT;
	}
}
