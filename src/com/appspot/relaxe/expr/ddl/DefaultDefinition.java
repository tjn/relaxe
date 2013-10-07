/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl;

import com.appspot.relaxe.expr.CompoundElement;
import com.appspot.relaxe.expr.Element;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.NiladicFunction;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.expr.VisitContext;


public class DefaultDefinition
    extends CompoundElement {

	
	public static final DefaultDefinition NULL = new DefaultDefinition();
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 8408650449581983111L;
	private ValueExpression value; 
    
    public DefaultDefinition() {
        this.value = null;
    }

    public DefaultDefinition(NiladicFunction nf) {
        if (nf == null) {
            throw new NullPointerException("'nf' must not be null");
        }
        
        this.value = nf;
    }
    
    public DefaultDefinition(ValueExpression element) {
        if (element == null) {
            throw new NullPointerException("element must not be null");
        }
        
        this.value = element;
    }
    
    @Override
    public void traverse(VisitContext vc, ElementVisitor v) {
    	v.start(vc, this);
        SQLKeyword.DEFAULT.traverse(vc, v);        
        Element e = (value == null) ? SQLKeyword.NULL : value;
        e.traverse(vc, v);
        v.end(this);
    }
    
    public ValueExpression getValue() {
		return value;
	}
    
}
