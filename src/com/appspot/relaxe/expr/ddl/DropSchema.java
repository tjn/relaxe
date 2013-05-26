/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.expr.VisitContext;

public class DropSchema
	extends Statement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8328836316629524899L;
	private Identifier schemaName;
	private SQLKeyword cascade;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected DropSchema() {
	}
				
	public DropSchema(Identifier schemaName) {
	    this(schemaName, null);
	}
	
	public DropSchema(Identifier schemaName, Boolean cascade) {
		super(Name.DROP_SCHEMA);
		
		if (schemaName == null) {
            throw new NullPointerException("'schemaName' must not be null");
        }
		
		this.schemaName = schemaName;
		this.cascade = cascade(cascade);
	}	
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		SQLKeyword.DROP.traverse(vc, v);		
		SQLKeyword.SCHEMA.traverse(vc, v);
		getSchemaName().traverse(vc, v);		
		traverseNonEmpty(this.cascade, vc, v);
	}

    public Identifier getSchemaName() {
        return schemaName;
    }	
}