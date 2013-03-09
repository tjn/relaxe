/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env;

import org.apache.log4j.Logger;

import fi.tnie.db.DefaultValueAssignerFactory;
import fi.tnie.db.DefaultValueExtractorFactory;
import fi.tnie.db.ValueAssignerFactory;
import fi.tnie.db.ValueExtractorFactory;
import fi.tnie.db.env.mysql.MySQLImplementation;
import fi.tnie.db.meta.impl.DefaultEnvironment;

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
