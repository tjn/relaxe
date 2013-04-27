/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;



public interface Constraint
	extends SchemaElement {
	
	enum Type {
		FOREIGN_KEY,
		PRIMARY_KEY
	}
	
	Type getType();
}
