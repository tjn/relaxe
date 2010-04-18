/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.Keyword;
import fi.tnie.db.expr.Statement;
import fi.tnie.db.expr.VisitContext;

public class CreateSchema
	extends Statement {

	private Identifier schemaName;	
	private Identifier authID;
				
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
		Keyword.CREATE.traverse(vc, v);		
		Keyword.SCHEMA.traverse(vc, v);
		getSchemaName().traverse(vc, v);
		
		if (this.authID != null) {
		    Keyword.AUTHORIZATION.traverse(vc, v);
		    this.authID.traverse(vc, v);
		}
		
		// TODO: schema-elemment-list
	}

    public Identifier getSchemaName() {
        return schemaName;
    }
}
