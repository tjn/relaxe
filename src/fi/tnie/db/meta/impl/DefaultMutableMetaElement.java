/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import fi.tnie.db.meta.MetaElement;

public abstract class DefaultMutableMetaElement 
	extends DefaultMutableMetaObject implements MetaElement {
	
	private DefaultMutableSchema schemaImpl;

	public DefaultMutableMetaElement() {
		super();
	}

	public DefaultMutableMetaElement(DefaultMutableSchema schema, String n) {
		super(n);
		setSchemaImpl(schema);
	}

	@Override
	public DefaultMutableSchema getSchema() {		
		return getSchemaImpl();
	}

	public DefaultMutableSchema getSchemaImpl() {
		return schemaImpl;
	}

	public void setSchemaImpl(DefaultMutableSchema schemaImpl) {
		this.schemaImpl = schemaImpl;
	}

	public void attach(DefaultMutableSchema s) {
		this.schemaImpl = s;
	}

	public void detach() {
		this.schemaImpl = null;
	}

	@Override
	public DefaultMutableMetaObject getParent() {		
		return getSchemaImpl();
	}
}
