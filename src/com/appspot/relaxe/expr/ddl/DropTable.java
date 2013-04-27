/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl;

import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.SchemaElementName;

public class DropTable
    extends DropSchemaElement {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6700704909715951859L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected DropTable() {
	}
	
    public DropTable(SchemaElementName name) {
        this(name, null);
    }
    
    public DropTable(SchemaElementName name, Boolean cascade) {
        super(Name.DROP_TABLE, SQLKeyword.TABLE, name, cascade);        
    }    
}
