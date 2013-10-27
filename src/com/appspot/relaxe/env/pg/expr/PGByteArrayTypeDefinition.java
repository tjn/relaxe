/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.pg.expr;

import com.appspot.relaxe.expr.Element;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.OrdinaryIdentifier;
import com.appspot.relaxe.expr.ddl.types.SQLArrayTypeDefinition;

public class PGByteArrayTypeDefinition
	extends SQLArrayTypeDefinition {
	
	public static final PGByteArrayTypeDefinition DEFINITION = new PGByteArrayTypeDefinition();
	
	public static final String NAME = "bytea";
	
	private static final Identifier TYPE_NAME = new OrdinaryIdentifier(NAME); 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 867305622555760454L;
	
	public PGByteArrayTypeDefinition() {
		super(PGTextTypeDefinition.DEFINITION);	
	}
	
	@Override
	public Element getTypeName() {
		return TYPE_NAME;
	}
}
