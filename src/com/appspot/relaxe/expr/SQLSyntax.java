/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import com.appspot.relaxe.expr.ddl.AlterTableDropConstraint;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.PrimaryKey;


public interface SQLSyntax {
    
    DeleteStatement newDeleteStatement(TableReference tref, Predicate p);    
    
    AlterTableDropConstraint newAlterTableDropForeignKey(ForeignKey fk);    
    AlterTableDropConstraint newAlterTableDropPrimaryKey(PrimaryKey pk);
    
    

}
