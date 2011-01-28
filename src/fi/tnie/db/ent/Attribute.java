/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;

import fi.tnie.db.types.PrimitiveType;

public interface Attribute
	extends Identifiable, Serializable {

	PrimitiveType<?> type();	
}
