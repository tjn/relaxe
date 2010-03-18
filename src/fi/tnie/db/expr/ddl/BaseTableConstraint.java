/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

import fi.tnie.db.expr.Identifier;

public interface BaseTableConstraint
    extends BaseTableElement {

    /**
     * SQLTypeName of the constraint.
     * @return
     */
    Identifier getName();    
}
