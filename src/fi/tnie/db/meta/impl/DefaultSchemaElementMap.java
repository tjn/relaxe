/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.meta.impl;

import fi.tnie.db.meta.Environment;
import fi.tnie.db.meta.SchemaElement;
import fi.tnie.db.meta.SchemaElementMap;

class DefaultSchemaElementMap<E extends SchemaElement>
	extends DefaultElementMap<E>
	implements SchemaElementMap<E> {
	
	public DefaultSchemaElementMap(Environment env) {
		super(env);
	}	
}