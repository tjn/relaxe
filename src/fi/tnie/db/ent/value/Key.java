/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.types.Type;

public interface Key<	
	T extends ReferenceType<?, ?, T, E, ?, ?, ?>,	
	E extends Entity<?, ?, T, E, ?, ?, ?>,
	V extends Type<V>,	
	K extends Key<T, E, V, ? extends K>
>
	extends Serializable
{
	V type();
	void copy(E src, E dest)
		throws EntityRuntimeException;
	
	/**
	 * TODO: Is this name sane?
	 * @param src
	 */
	void clear(E src)
		throws EntityRuntimeException;
	
	K self();
}
