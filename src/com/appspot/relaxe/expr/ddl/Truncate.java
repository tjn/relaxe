/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.meta.BaseTable;

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
