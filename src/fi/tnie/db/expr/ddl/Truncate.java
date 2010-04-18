/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.Keyword;
import fi.tnie.db.expr.SchemaElementName;
import fi.tnie.db.expr.Statement;
import fi.tnie.db.expr.VisitContext;
import fi.tnie.db.meta.BaseTable;

public class Truncate
    extends Statement {

    private SchemaElementName table;
    
    public Truncate(BaseTable table) {
        super(Name.TRUNCATE);
        
        if (table == null) {
            throw new NullPointerException("'table' must not be null");
        }
        
        this.table = relativize(table.getName());
    }
    
    
    @Override
    protected void traverseContent(VisitContext vc, ElementVisitor v) {
        Keyword.TRUNCATE.traverse(vc, v);        
        Keyword.TABLE.traverse(vc, v);
        this.table.traverse(vc, v);        
    }

}
