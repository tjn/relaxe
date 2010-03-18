/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

import fi.tnie.db.expr.Keyword;
import fi.tnie.db.expr.SchemaElementName;

public class DropView
    extends DropSchemaElement {

    public DropView(SchemaElementName name) {
        this(name, null);
    }
    
    public DropView(SchemaElementName name, Boolean cascade) {
        super(Name.DROP_CONSTRAINT, Keyword.CONSTRAINT, name, cascade);        
    }    
}
