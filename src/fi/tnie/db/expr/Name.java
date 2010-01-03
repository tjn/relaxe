/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.meta.Table;



public class Name
	extends CompoundElement {
	
	private Identifier schemaName;
	private Identifier objectName;
		
	public Name(Table table) {
		super();
		this.schemaName = new OrdinaryIdentifier(table.getSchema().getName());
		this.objectName = new OrdinaryIdentifier(table.getName());
	}
	
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		this.schemaName.traverse(vc, v);
		Symbol.DOT.traverse(vc, v);
		this.objectName.traverse(vc, v);
	}
	
	
}
