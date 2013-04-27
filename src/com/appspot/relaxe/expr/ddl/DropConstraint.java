/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Constraint;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.PrimaryKey;

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
