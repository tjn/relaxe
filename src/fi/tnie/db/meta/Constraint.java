/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;



public interface Constraint
	extends SchemaElement {
	
	enum Type {
		FOREIGN_KEY,
		PRIMARY_KEY
	}
	
	Type getType();
}
