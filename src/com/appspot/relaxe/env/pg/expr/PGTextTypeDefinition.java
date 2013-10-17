/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.pg.expr;

import com.appspot.relaxe.expr.Element;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.OrdinaryIdentifier;
import com.appspot.relaxe.expr.ddl.types.AbstractCharacterTypeDefinition;

public class PGTextTypeDefinition
	extends AbstractCharacterTypeDefinition {
	
		/**
	 * 
	 */
	private static final long serialVersionUID = 867305622555760454L;
	
		
	public static final String NAME = "text";
	
	private static final Identifier TYPE_NAME = new OrdinaryIdentifier(NAME);
	
	public static final PGTextTypeDefinition DEFINITION = new PGTextTypeDefinition();
	
	@Override
	public Name getSQLTypeName() {
		return null;
	}
	
	@Override
	public Element getTypeName() {
		return TYPE_NAME;
	}	
}
