/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl;

import com.appspot.relaxe.expr.Identifier;

public interface BaseTableConstraint
    extends BaseTableElement {

    /**
     * SQLTypeName of the constraint.
     * @return
     */
    Identifier getName();    
}
