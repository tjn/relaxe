/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import java.util.Comparator;

import com.appspot.relaxe.expr.Identifier;


public abstract class DefaultEnvironment 
	implements SerializableEnvironment {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6518870057408097303L;
	private IdentifierRules identifierRules = new SQLIdentifierRules();
	
	private final SchemaElementMap<ForeignKey> emptyForeignKeyMap = new EmptyForeignKeyMap(this); 
				
		protected Comparator<Identifier> createIdentifierComparator() {
		return FoldingComparator.UPPERCASE;
	}
	
		
	public SchemaElementMap<ForeignKey> emptyForeignKeyMap()  {
		return this.emptyForeignKeyMap;
	}
	
	@Override
	public IdentifierRules getIdentifierRules() {
		return identifierRules;
	}
}
