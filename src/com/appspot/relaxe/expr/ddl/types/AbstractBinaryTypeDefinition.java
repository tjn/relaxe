/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.IntLiteral;
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.VisitContext;

public abstract class AbstractBinaryTypeDefinition
    extends SQLTypeDefinition {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 7291068267695357641L;
	private IntLiteral length = null;
    
    /**
	 * No-argument constructor for GWT Serialization
	 */
	protected AbstractBinaryTypeDefinition() {
	}
    
    protected AbstractBinaryTypeDefinition(Integer length) {                        
        if (length != null && length.intValue() < 1) {
            throw new IllegalArgumentException("illegal varchar length: " + length);
        }
        
        this.length = (length == null) ? null : IntLiteral.valueOf(length.intValue());
    }    
    
    @Override
    public void traverse(VisitContext vc, ElementVisitor v) {
        getTypeName().traverse(vc, v);
        
        if (this.length != null) {
	        Symbol.PAREN_LEFT.traverse(vc, v);
	        this.length.traverse(vc, v);        
	        Symbol.PAREN_RIGHT.traverse(vc, v);
        }
    }
}