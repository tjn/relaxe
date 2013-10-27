/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.VisitContext;

public class DoubleTypeDefinition
    extends FloatingPointDefinition {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -269346141333155275L;

	public static final DoubleTypeDefinition DEFINITION = new DoubleTypeDefinition();
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	private DoubleTypeDefinition() {		
	}
	
    @Override
    public SQLTypeDefinition.Name getSQLTypeName() {
    	return SQLTypeDefinition.Name.DOUBLE;
    }
            
    @Override
    public void traverse(VisitContext vc, ElementVisitor v) {
    	v.start(vc, this);
    	SQLKeyword.DOUBLE.traverse(vc, v);
    	SQLKeyword.PRECISION.traverse(vc, v);
    	v.end(this);
    }
}
