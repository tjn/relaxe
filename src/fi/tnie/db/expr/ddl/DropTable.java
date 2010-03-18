/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

import fi.tnie.db.expr.Keyword;
import fi.tnie.db.expr.SchemaElementName;

public class DropTable
    extends DropSchemaElement {

    public DropTable(SchemaElementName name) {
        this(name, null);
    }
    
    public DropTable(SchemaElementName name, Boolean cascade) {
        super(Name.DROP_TABLE, Keyword.TABLE, name, cascade);        
    }    
}
