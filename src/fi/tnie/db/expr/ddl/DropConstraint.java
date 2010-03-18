/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

import fi.tnie.db.expr.Keyword;
import fi.tnie.db.expr.SchemaElementName;

public class DropConstraint
    extends DropSchemaElement {

    public DropConstraint(SchemaElementName name) {
        this(name, null);
    }
    
    public DropConstraint(SchemaElementName name, Boolean cascade) {
        super(Name.DROP_VIEW, Keyword.VIEW, name, cascade);        
    }    
}
