/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model.cm;

import fi.tnie.db.model.Registration;
import fi.tnie.db.model.ValueModel;

public interface ConstrainedValueModel<V>
	extends ValueModel<V> {
	
	Registration addConstraint(Constraint ph);
	
	/**
	 * Creates a new proposition. 
	 * @param newValue
	 * @return
	 */	
	Proposition propose(ChangeSet cs, V newValue, Proposition impliedBy);
	
	/**
	 * Submits the proposition and returns it.  
	 * 
	 * @param cs
	 * @param p
	 * @return
	 */	
	Proposition submit(ChangeSet cs, Proposition p);
	
	/**
	 * If the this model contains a proposition for this model within
	 * the specified change set, returns the proposed value. 
	 * Otherwise, returns the current value of the model. 
	 *   
	 * @param cs
	 * @return
	 */
	V proposed(ChangeSet cs);
}
