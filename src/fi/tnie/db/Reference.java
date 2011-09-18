/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.io.Serializable;

import fi.tnie.db.ent.Identifiable;
import fi.tnie.db.types.ReferenceType;

public interface Reference
	extends Identifiable, Serializable {

	ReferenceType<?, ?, ?, ?, ?, ?, ?> type();
}
