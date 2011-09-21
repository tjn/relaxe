/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.meta.impl;

import fi.tnie.db.meta.SchemaElement;
import fi.tnie.db.meta.SchemaElementMap;
import fi.tnie.db.meta.SerializableEnvironment;

public class DefaultSchemaElementMap<E extends SchemaElement>
	extends DefaultElementMap<E>
	implements SchemaElementMap<E> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4254756545832805692L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected DefaultSchemaElementMap() {
	}
	
	public DefaultSchemaElementMap(SerializableEnvironment env) {
		super(env);
	}	
}