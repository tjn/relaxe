/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.VisitContext;

public class SQLArrayTypeDefinition
    extends SQLTypeDefinition {
       	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4432919211852344906L;
	private SQLTypeDefinition elementType;


	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private SQLArrayTypeDefinition() {
	}
	
	public SQLArrayTypeDefinition(SQLTypeDefinition elementType) {
		this.elementType = elementType;
	}
	
	@Override
	public Name getSQLTypeName() {
		return SQLTypeDefinition.Name.ARRAY;
	}
	

    @Override
    public void traverse(VisitContext vc, ElementVisitor v) {
    	v.start(vc, this);
    	elementType.traverse(vc, v);
    	Symbol.BRACKET_LEFT.traverse(vc, v);
    	Symbol.BRACKET_RIGHT.traverse(vc, v);    	
    	v.end(this);
    }
    
    public SQLTypeDefinition getElementType() {
		return elementType;
	}
}
