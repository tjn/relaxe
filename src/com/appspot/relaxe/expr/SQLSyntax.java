/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;


public interface SQLSyntax {
    
    DeleteStatement newDeleteStatement(TableReference tref, Predicate p);
        

}
