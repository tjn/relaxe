/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.SQLKeyword;
import fi.tnie.db.expr.SchemaElementName;
import fi.tnie.db.expr.Statement;
import fi.tnie.db.expr.VisitContext;
import fi.tnie.db.meta.BaseTable;

public class Truncate
    extends Statement {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2372791261527314160L;
	private SchemaElementName table;
    
    /**
	 * No-argument constructor for GWT Serialization
	 */
	protected Truncate() {
	}
    
    public Truncate(BaseTable table) {
        super(Name.TRUNCATE);
        
        if (table == null) {
            throw new NullPointerException("'table' must not be null");
        }
        
        this.table = relativize(table.getName());
    }
    
    
    @Override
    protected void traverseContent(VisitContext vc, ElementVisitor v) {
        SQLKeyword.TRUNCATE.traverse(vc, v);        
        SQLKeyword.TABLE.traverse(vc, v);
        this.table.traverse(vc, v);        
    }

}
