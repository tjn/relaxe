/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.io.Serializable;

import fi.tnie.db.expr.Identifier;

public interface MetaObject
	extends Serializable {
	public Identifier getUnqualifiedName();
	
}
