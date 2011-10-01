/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.SQLKeyword;
import fi.tnie.db.expr.SchemaElementName;
import fi.tnie.db.expr.Statement;
import fi.tnie.db.expr.VisitContext;

public abstract class DropSchemaElement 
    extends Statement
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
    
   
