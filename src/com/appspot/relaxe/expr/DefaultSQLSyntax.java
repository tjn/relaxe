/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import com.appspot.relaxe.expr.ddl.AlterTableDropConstraint;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.PrimaryKey;

public class DefaultSQLSyntax
    implements SQLSyntax {

    @Override
    public DeleteStatement newDeleteStatement(TableReference tref, Predicate p) {
        return new DeleteStatement(tref, p);
    }

    
    @Override
    public AlterTableDropConstraint newAlterTableDropForeignKey(ForeignKey fk) {
       	return new AlterTableDropConstraint(fk);
    }


	@Override
	public AlterTableDropConstraint newAlterTableDropPrimaryKey(PrimaryKey pk) {
       	return new AlterTableDropConstraint(pk);
	}
    
}
