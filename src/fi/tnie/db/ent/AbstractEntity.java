/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.types.ReferenceType;


public abstract class AbstractEntity<
	K,
	R, 
	T extends ReferenceType<T>,
	E extends Entity<K, R, T, ? extends E>
> 
	implements Entity<K, R, T, E>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1538787348338709153L;	
	

}
 