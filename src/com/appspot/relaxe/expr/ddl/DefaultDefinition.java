/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl;

import com.appspot.relaxe.expr.CompoundElement;
import com.appspot.relaxe.expr.Element;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.NiladicFunction;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.VisitContext;


public class DefaultDefinition
    extends CompoundElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8408650449581983111L;
	private Element value; 
    
//    TODO: DefaultDefinition(Literal) {
//    }
    
    public DefaultDefinition() {
        this.value = SQLKeyword.NULL;
    }

    public DefaultDefinition(NiladicFunction nf) {
        if (nf == null) {
            throw new NullPointerException("'nf' must not be null");
        }
        
        this.value = nf;
    }
    
    @Override
    public void traverse(VisitContext vc, ElementVisitor v) {
        SQLKeyword.DEFAULT.traverse(vc, v);
        this.value.traverse(vc, v);
    }
}
