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
	private Proposition impliedBy;
	
	public SimpleProposition(ValueModel<V> from, V to) {
		this(from, to, null);
	}
	 			
	public SimpleProposition(ValueModel<V> from, V to, Proposition impliedBy) {
		super();		
		this.from = from.get();
		this.to = to;		
		this.impliedBy = impliedBy;
	}

	public V from() {
		return this.from;
	}

	public V to() {
		return this.to;
	}
	
	@Override	
	public Proposition impliedBy() {
		return this.impliedBy;
	}
}