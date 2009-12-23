package fi.tnie.db.meta.impl;

import fi.tnie.db.meta.Constraint;

public abstract class ConstraintImpl
	extends DefaultMutableMetaElement
	implements Constraint {

	public ConstraintImpl() {
		super();		
	}

	public ConstraintImpl(DefaultMutableSchema schema, String n) {
		super(schema, n);	
	}
}
