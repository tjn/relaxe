/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.expr.VisitContext;

public class CreateSchema
	extends SQLSchemaStatement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7627361956865390823L;
	private Identifier schemaName;	
	private Identifier authID;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected CreateSchema() {
	}
				
	public CreateSchema(Identifier name) {
	    this(name, null);
	}
	
	public CreateSchema(Identifier schemaName, Identifier authID) {
		super(Name.CREATE_SCHEMA);
		
		if (schemaName == null) {
            throw new NullPointerException("'schemaName' must not be null");
        }
		
		this.schemaName = schemaName;
		this.authID = authID;
	}	
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		SQLKeyword.CREATE.traverse(vc, v);		
		SQLKeyword.SCHEMA.traverse(vc, v);
		getSchemaName().traverse(vc, v);
		
		if (this.authID != null) {
		    SQLKeyword.AUTHORIZATION.traverse(vc, v);
		    this.authID.traverse(vc, v);
		}
		
		// TODO: schema-element-list
	}

    public Identifier getSchemaName() {
        return schemaName;
    }
}
