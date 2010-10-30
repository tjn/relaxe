/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.types.ReferenceType;


public abstract class AbstractEntity<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	T extends ReferenceType<T>,
	E extends Entity<A, R, Q, T, ? extends E>
> 
	implements Entity<A, R, Q, T, E>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1538787348338709153L;	
	

}
 