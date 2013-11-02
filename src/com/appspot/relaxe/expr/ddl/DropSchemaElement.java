/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.VisitContext;

public abstract class DropSchemaElement 
    extends SQLSchemaStatement
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4367840517906725303L;
	private SQLKeyword elementType;
    private SQLKeyword cascade;
    private SchemaElementName name;
    
    /**
	 * No-argument constructor for GWT Serialization
	 */
	protected DropSchemaElement() {
	}
    
    protected DropSchemaElement(Name statementName, SQLKeyword elementType, SchemaElementName name, Boolean cascade) {
        super(statementName);
        
        if (name == null) {
            throw new NullPointerException("'name' must not be null");
        }
        
        if (elementType == null) {
            throw new NullPointerException("'elementType' must not be null");
        }
        
        this.name = relativize(name);        
        this.elementType = elementType;
        this.cascade = cascade(cascade);
    }    
    
    @Override
    protected void traverseContent(VisitContext vc, ElementVisitor v) {
        SQLKeyword.DROP.traverse(vc, v);        
        this.elementType.traverse(vc, v);                
        this.name.traverse(vc, v);
        traverseNonEmpty(this.cascade, vc, v);
    }
}
    
   
