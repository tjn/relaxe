/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.meta.impl.mariadb;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.ddl.DefaultDefinition;
import com.appspot.relaxe.meta.AbstractIdentifierComparator;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataTypeMap;
import com.appspot.relaxe.meta.DefaultDataTypeMap;
import com.appspot.relaxe.meta.NullComparator;
import com.appspot.relaxe.meta.SerializableEnvironment;

public class MariaDBEnvironment
	implements SerializableEnvironment {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2968383567147338099L;
		
	private static MariaDBEnvironment instance;
	private final transient MariaDBIdentifierRules identifierRules = new MariaDBIdentifierRules();
	private MariaDBDataTypeMap dataTypeMap = new MariaDBDataTypeMap();
	
	public static class IdentifierComparator
		extends AbstractIdentifierComparator {
		
		private static final NullComparator.CaseInsensitiveString nameComparator = new NullComparator.CaseInsensitiveString();
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -7036929045437474118L;

		@Override
		protected int compare(String n1, String n2) {
			return nameComparator.compare(n1, n2);
		}

		@Override
		protected String name(Identifier ident) {
			if (ident == null) {
				return null;
			}
			
			String n = ident.getName();
			return n; 
		}
	}
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private MariaDBEnvironment() {
	}	
	
	public static MariaDBEnvironment environment() {
		if (instance == null) {
			instance = new MariaDBEnvironment();			
		}

		return instance;
	}
	
	@Override
	public MariaDBIdentifierRules getIdentifierRules() {
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
	
	private static class MariaDBDataTypeMap
		extends DefaultDataTypeMap {

		@Override
		protected SchemaElementName newName(String typeName) {
			return environment().getIdentifierRules().newName(typeName);
		}		
	}
}