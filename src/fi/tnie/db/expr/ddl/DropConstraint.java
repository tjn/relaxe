/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.Keyword;
import fi.tnie.db.expr.SchemaElementName;
import fi.tnie.db.expr.Statement;
import fi.tnie.db.expr.VisitContext;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Constraint;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.PrimaryKey;

public class DropConstraint
    extends Statement {

    private SchemaElementName table; 
    private SchemaElementName constraint;

    public DropConstraint(PrimaryKey pk) {
        this(pk, pk.getTable());
    }

    public DropConstraint(ForeignKey fk) {
        this(fk, fk.getReferencing());
    }
    
    private DropConstraint(Constraint constraint, BaseTable t) {
        super(Name.DROP_CONSTRAINT);
        this.constraint = constraint.getName();        
        this.table = relativize(t.getName());
    }
    
    @Override
    protected void traverseContent(VisitContext vc, ElementVisitor v) {
        Keyword.ALTER.traverse(vc, v);
        Keyword.TABLE.traverse(vc, v);        
        this.table.traverse(vc, v);
        Keyword.DROP.traverse(vc, v);        
        Keyword.CONSTRAINT.traverse(vc, v);
        this.constraint.getUnqualifiedName().traverse(vc, v);                
    }
}
