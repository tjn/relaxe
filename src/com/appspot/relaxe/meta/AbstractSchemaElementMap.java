/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import java.util.Map;

import com.appspot.relaxe.expr.Identifier;

public abstract class AbstractSchemaElementMap<E extends SchemaElement>
	extends AbstractMetaObjectMap<E>
	implements SchemaElementMap<E> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5772991462726927793L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AbstractSchemaElementMap() {
	}
	
	protected AbstractSchemaElementMap(Environment environment, Map<Identifier, E> content) {
		super(environment, content);
	}
}
