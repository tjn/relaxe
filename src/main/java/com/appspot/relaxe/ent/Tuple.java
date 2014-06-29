package com.appspot.relaxe.ent;

import java.io.Serializable;

/**
 * Immutable ordered list of nun-null elements.
 * 
 * @param <E> The type of tuple the elements.
 */
public interface Tuple<E extends Serializable>
	extends Serializable {
	
	/**
	 * The number of the elements.
	 * 
	 * @return
	 */
	int size();
	
	E get(int index);
	
	/**
	 * Returns <code>true</code> if and only of t is a tuple with same size and equals elements than this.   
	 * 
	 * @param t
	 * @return
	 */		
	@Override
	boolean equals(Object t);
	
	/**
	 * Returns <code>true</code> if and only of t is a tuple with same size and equals elements than this.   
	 * 
	 * @param t
	 * @return
	 */	
	@Override
	int hashCode();	
}
