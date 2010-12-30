/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.model.cm;

import fi.tnie.db.model.Change;
import fi.tnie.db.model.ValueModel;

public abstract class SimpleProposition<V>
	extends AbstractProposition
	implements Change<V> {
	
	private V from;	
	private V to;		
	 			
	SimpleProposition(ValueModel<V> from, V to) {
		super();		
		this.from = from.get();
		this.to = to;
	}

	public V from() {
		return this.from;
	}

	public V to() {
		return this.to;
	}	
}