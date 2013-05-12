/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import java.io.Serializable;

import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.types.AbstractType;


public interface Key<
	E,
	V extends AbstractType<V>,	
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
