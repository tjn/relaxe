/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;

import fi.tnie.db.types.ReferenceType;

public interface Reference extends Identifiable, Serializable {
	ReferenceType<?, ?, ?, ?, ?, ?, ?> type();
		
}
