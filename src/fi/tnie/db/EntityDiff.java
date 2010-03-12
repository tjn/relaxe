/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;


import java.util.Map;

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
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,	
	Q extends Enum<Q> & Identifiable,
	E extends Entity<A, R, Q, ? extends E>>
{	
	enum Change {
		ADDITION,
		DELETION,
		MODIFICATION
	}
	
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
	
	/**  
	 * @return
	 */
	Map<A, Change> attributes();	
	Map<R, Change> references();	
}
