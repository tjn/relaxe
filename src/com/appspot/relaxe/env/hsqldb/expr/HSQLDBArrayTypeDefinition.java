/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.hsqldb.expr;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.IntLiteral;
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.expr.ddl.types.SQLTypeDefinition;

public class HSQLDBArrayTypeDefinition
    extends SQLTypeDefinition {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3676886379250567100L;
	
	private SQLTypeDefinition elementType;
	private IntLiteral size;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private HSQLDBArrayTypeDefinition() {
	}
	
    public HSQLDBArrayTypeDefinition(SQLTypeDefinition elementType, Integer size) {
		super();
		this.elementType = elementType;
		this.size = (size == null) ? null : IntLiteral.valueOf(size.intValue());
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
        v.start(vc, this);
    
        this.elementType.traverse(vc, v);
        HSQLDBKeyword.ARRAY.traverse(vc, v);
        
        if (size != null) {
        	Symbol.BRACKET_LEFT.traverse(vc, v);
        	this.size.traverse(vc, v);
        	Symbol.BRACKET_RIGHT.traverse(vc, v);
        }        
        
        v.end(this);        
    }

	@Override
	public Name getSQLTypeName() {
		return null;
	}
}
