/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.io.Serializable;

import fi.tnie.db.expr.Identifier;

public class DefaultMutableView 
	extends DefaultMutableTable 
	implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -8583771068698486683L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected DefaultMutableView() {
	}
	
	public DefaultMutableView(DefaultMutableSchema s, Identifier name) {
		super(s, name);
	}

	@Override
	public String getTableType() {
		return VIEW;
	}

	@Override
	public boolean isBaseTable() {
		return false;
	}
	
	@Override
	public String toString() {		
		return "view " + getQualifiedName() + "@" + System.identityHashCode(this) + ": schema=" + getSchema() + ": cat=" + getSchema().getCatalog();
	}
	
}
