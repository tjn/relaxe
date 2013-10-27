/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.meta.impl.hsqldb;

import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.meta.DataTypeMap;
import com.appspot.relaxe.meta.DefaultDataTypeMap;
import com.appspot.relaxe.meta.DefaultEnvironment;

public class HSQLDBEnvironment 
	extends DefaultEnvironment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1541050975229495155L;
	
	private static HSQLDBEnvironment environment = new HSQLDBEnvironment();	
	private DataTypeMap dataTypeMap = new HSQLDBDataTypeMap();
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	private HSQLDBEnvironment() {
	}
	
	public static HSQLDBEnvironment environment() {
		return HSQLDBEnvironment.environment;
	}	

	@Override
	public DataTypeMap getDataTypeMap() {
		return dataTypeMap;
	}

	private static class HSQLDBDataTypeMap
		extends DefaultDataTypeMap {
		
		
		
		

		@Override
		protected SchemaElementName newName(String typeName) {
			return HSQLDBEnvironment.environment().getIdentifierRules().newName(typeName);
		}		
	}
	
}