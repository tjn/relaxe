/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import fi.tnie.db.expr.Identifier;


public interface Catalog
	extends MetaObject {
	
	SerializableEnvironment getEnvironment();
	
//	Comparator<Identifier> identifierComparator();
//	
//	Identifier create(String name)
//		throws IllegalIdentifierException;
	
	SchemaMap schemas();
	
//	Set<Identifier> schemaNames();
//	Schema getSchema(Identifier name);
//	
//	Schema getSchema(String name)
//		throws IllegalIdentifierException;
//	
//	ElementMap<? extends Schema> schemas();
	
	Identifier getName();		
}
