/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.SQLKeyword;
import fi.tnie.db.expr.SchemaElementName;
import fi.tnie.db.expr.Statement;
import fi.tnie.db.expr.VisitContext;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Constraint;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.PrimaryKey;

public class DropConstraint
    extends Statement {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6604968506968220728L;
	private SchemaElementName table; 
    private SchemaElementName constraint;
    
    /**
	 * No-argument constructor for GWT Serialization
	 */
	protected DropConstraint() {
	}

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
        SQLKeyword.ALTER.traverse(vc, v);
        SQLKeyword.TABLE.traverse(vc, v);        
        this.table.traverse(vc, v);
        SQLKeyword.DROP.traverse(vc, v);        
        SQLKeyword.CONSTRAINT.traverse(vc, v);
        this.constraint.getUnqualifiedName().traverse(vc, v);                
    }
}
