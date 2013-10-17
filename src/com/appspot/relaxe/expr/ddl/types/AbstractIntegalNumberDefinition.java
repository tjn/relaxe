/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.IntLiteral;
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.VisitContext;

public abstract class AbstractIntegalNumberDefinition
    extends AbstractNumericTypeDefinition {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 2776206428444898457L;
	private Integer precision;

    protected AbstractIntegalNumberDefinition() {      
        this(null);
    }
    
    protected AbstractIntegalNumberDefinition(Integer precision) {
        this.precision = precision;
    }
    
    @Override
    public boolean isExact() {     
        return true;
    }
    
    public Integer getPrecision() {
		return precision;
	}
    
    @Override
    public void traverse(VisitContext vc, ElementVisitor v) {
    	v.start(vc, this);
    	getSQLTypeName().traverse(vc, v);
    	
    	Integer p = getPrecision();
    	IntLiteral pe = (p == null) ? null : IntLiteral.valueOf(p.intValue());
    	
    	if (pe != null) {
    		Symbol.PAREN_LEFT.traverse(vc, v);
    		pe.traverse(vc, v);    		
    		Symbol.PAREN_RIGHT.traverse(vc, v);
    	}    	
    	
    	v.end(this);    	
    }
}
