/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.Constraint;

public abstract class DefaultConstraint
	extends DefaultSchemaElement
	implements Constraint {

	DefaultConstraint(DefaultMutableSchema schema, Identifier name) {
		super(schema, name);
	}

}
