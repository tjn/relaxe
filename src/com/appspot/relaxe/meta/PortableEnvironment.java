/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import com.appspot.relaxe.expr.ddl.DefaultDefinition;

public class PortableEnvironment
	implements SerializableEnvironment {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 4031007968601315447L;

	private final PortableIdentifierRules identifierRules = new PortableIdentifierRules();
	
	public static PortableEnvironment environment = new PortableEnvironment();
	
	public static PortableEnvironment environment() {
		return PortableEnvironment.environment;
	}	
	
	@Override
	public PortableIdentifierRules getIdentifierRules() {
		return identifierRules;
	}

	@Override
	public DefaultDefinition newDefaultDefinition(Column col) {
		return null;
	}	
}
