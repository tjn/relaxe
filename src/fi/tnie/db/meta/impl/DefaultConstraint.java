/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.Constraint;

public abstract class DefaultConstraint
	extends DefaultSchemaElement
	implements Constraint {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1698615492918408242L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected DefaultConstraint() {
	}

	DefaultConstraint(DefaultMutableSchema schema, Identifier name) {
		super(schema, name);
	}

}
