/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.SchemaElementName;

public interface SchemaElement
	extends MetaObject {
	Schema getSchema();	
	public SchemaElementName getName();
	
	Identifier getUnqualifiedName();
	String getQualifiedName();
}
