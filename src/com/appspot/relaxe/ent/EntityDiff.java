/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;


import java.util.Map;

import com.appspot.relaxe.types.ReferenceType;


/**
 * Represents the difference between two entities.
 *
 * @author Administrator
 *
 * @param <A>
 * @param <R>
 * @param <Q>
 * @param <E>
 */

public interface EntityDiff<
	A extends Attribute,
	R extends Reference,	
	T extends ReferenceType<A, R, T, E, ?, ?, ?, ?>,
	E extends Entity<A, R, T, E, ?, ?, ?, ?>>
{
	/**
	 * "Total" difference between original and modified entity.
	 *
	 *  Return value <code>null</code> indicates that there are no
	 *  changes between original and modified.
	 *
	 *  Return value {@link Change.ADDITION} indicates that
	 *  the original is <code>null</code>.
	 *
	 *  Return value {@link Change.DELETION} indicates that
	 *  the modified is <code>null</code>

 	 *  Return value {@link Change.MODIFICATION} indicates that
	 *  either {@link attributes()} or {@link references()} is not empty.
	 *
	 * @return The change
	 */
	Change change();

	E getOriginal();
	E getModified();

	/**	 *
	 * @return
	 */
	Map<A, Change> attributes();

	/**
	 * @return
	 */
	Map<R, Change> references();
}
