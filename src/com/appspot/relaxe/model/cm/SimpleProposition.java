/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.model.cm;

import com.appspot.relaxe.model.Change;
import com.appspot.relaxe.model.ValueModel;

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

	@Override
	public V from() {
		return this.from;
	}

	@Override
	public V to() {
		return this.to;
	}
	
	@Override	
	public Proposition impliedBy() {
		return this.impliedBy;
	}
}