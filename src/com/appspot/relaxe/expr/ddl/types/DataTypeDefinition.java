/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;


import com.appspot.relaxe.expr.CompoundElement;
import com.appspot.relaxe.expr.Element;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.expr.ddl.ColumnDataType;

public abstract class DataTypeDefinition
    extends CompoundElement 
    implements ColumnDataType {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -8759055758167527360L;
    
    public abstract Element getTypeName();
    
    @Override
    public void traverse(VisitContext vc, ElementVisitor v) {
        v.start(vc, this);
        traverseNonEmpty(getTypeName(), vc, v);
        v.end(this);
    }
}
