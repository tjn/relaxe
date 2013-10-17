/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;


public final class SmallIntTypeDefinition
	extends AbstractIntegalNumberDefinition {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4142797245840237139L;
	public static final SmallIntTypeDefinition DEFINITION = new SmallIntTypeDefinition();
	

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected SmallIntTypeDefinition() {
	}


	public SmallIntTypeDefinition(Integer precision) {
		super(precision);	
	}

	public SmallIntTypeDefinition(int precision) {
		super(Integer.valueOf(precision));
	}

	public static SmallIntTypeDefinition get(Integer precision) {		
		return (precision == null) ? DEFINITION : new SmallIntTypeDefinition(precision);
	}

	@Override
	public SQLTypeDefinition.Name getSQLTypeName() {
		return SQLTypeDefinition.Name.SMALLINT;
	}
}
