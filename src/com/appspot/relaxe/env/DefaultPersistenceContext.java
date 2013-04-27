/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env;

import com.appspot.relaxe.ValueAssignerFactory;
import com.appspot.relaxe.ValueExtractorFactory;

/**
 * TODO: This needs to be fixed. DefaultImplementation should not extend a DefaultEnvironment.
 * We should not to require implementation object to be serializable.
 */

public abstract class DefaultPersistenceContext<I extends Implementation<I>>	
	implements PersistenceContext<I> {
	
	private I implementation;
	
	protected DefaultPersistenceContext() {
	}
	
	public DefaultPersistenceContext(I implementation) {
		super();
		
		if (implementation == null) {
			throw new NullPointerException("implementation");
		}
		
		this.implementation = implementation;
	}
	
	@Override
	public ValueAssignerFactory getValueAssignerFactory() {
		return getImplementation().getValueAssignerFactory();
	}

	@Override
	public ValueExtractorFactory getValueExtractorFactory() {
		return getImplementation().getValueExtractorFactory();		
	}

	@Override
	public I getImplementation() {
		return this.implementation;
	}	
}
