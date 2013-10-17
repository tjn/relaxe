/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.ddl.DefaultDefinition;

public class PortableEnvironment
	implements SerializableEnvironment {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 4031007968601315447L;

	private final PortableIdentifierRules identifierRules = new PortableIdentifierRules();
	
	private static PortableEnvironment environment = new PortableEnvironment();
	private DataTypeMap dataTypeMap = new PortableDataTypeMap();	
	
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
	
	@Override
	public DataTypeMap getDataTypeMap() {
		return this.dataTypeMap;
	}
	
	private static class PortableDataTypeMap
		extends DefaultDataTypeMap {
	
		@Override
		protected SchemaElementName newName(String typeName) {
			return PortableEnvironment.environment().getIdentifierRules().newName(typeName);
		}		
	}	
	
}
