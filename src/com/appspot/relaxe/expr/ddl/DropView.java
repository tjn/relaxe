/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl;

import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.SchemaElementName;

public class DropView
    extends DropSchemaElement {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6834997046796237907L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected DropView() {
	}

    public DropView(SchemaElementName name) {
        this(name, null);
    }
    
    public DropView(SchemaElementName name, Boolean cascade) {
        super(Name.DROP_VIEW, SQLKeyword.VIEW, name, cascade);        
    }    
}
