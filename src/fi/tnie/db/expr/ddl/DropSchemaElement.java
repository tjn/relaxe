/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.Keyword;
import fi.tnie.db.expr.SchemaElementName;
import fi.tnie.db.expr.Statement;
import fi.tnie.db.expr.VisitContext;
import fi.tnie.db.expr.Statement.Name;

public abstract class DropSchemaElement 
    extends Statement
{
    private Keyword elementType;
    private Keyword cascade;
    private SchemaElementName name;
    
    protected DropSchemaElement(Name statementName, Keyword elementType, SchemaElementName name, Boolean cascade) {
        super(statementName);
        
        if (name == null) {
            throw new NullPointerException("'name' must not be null");
        }
        
        if (elementType == null) {
            throw new NullPointerException("'elementType' must not be null");
        }
        
        this.name = name;        
        this.elementType = elementType;
        this.cascade = cascade(cascade);
    }    
    
    @Override
    protected void traverseContent(VisitContext vc, ElementVisitor v) {
        Keyword.DROP.traverse(vc, v);        
        this.elementType.traverse(vc, v);                
        this.name.traverse(vc, v);
        traverseNonEmpty(this.cascade, vc, v);
    }
}
    
   
