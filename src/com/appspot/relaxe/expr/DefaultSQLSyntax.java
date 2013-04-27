/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

public class DefaultSQLSyntax
    implements SQLSyntax {

    @Override
    public DeleteStatement newDeleteStatement(TableReference tref, Predicate p) {
        return new DeleteStatement(tref, p);
    }

    
}
