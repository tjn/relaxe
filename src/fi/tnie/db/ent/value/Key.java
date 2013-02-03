/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.types.Type;

public interface Key<
	E,
	V extends Type<V>,	
	K extends Key<E, V, K>
>
	extends Serializable
{
	V type();
	
	void copy(E src, E dest)
		throws EntityRuntimeException;
	
	/**
	 * Assign the default value for the attribute or reference addressed by this key. 
	 * 
	 * @param dest
	 */
	void reset(E dest)
		throws EntityRuntimeException;
	
	K self();
}
