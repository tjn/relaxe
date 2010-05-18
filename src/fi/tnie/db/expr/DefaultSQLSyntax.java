/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class DefaultSQLSyntax
    implements SQLSyntax {

    @Override
    public DeleteStatement newDeleteStatement(TableReference tref, Predicate p) {
        return new DeleteStatement(tref, p);
    }

    
}
