/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model.cm;


/**
 * Provides an abstraction to the rules by which the proposition 
 * is rejected or committed.
 *  
 * @author tnie
 */
public interface Constraint {
	
	/**
	 * Rejects the proposition <code>p</code> if it violates this constraint.
	 * 
	 * @param cs
	 * @param p
	 */
	void apply(ChangeSet cs, Proposition p);	
}
