/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.test;

import fi.tnie.db.ValueAssignerFactory;
import fi.tnie.db.ValueExtractorFactory;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.env.pg.PGPersistenceContext;

public class PagilaPersistenceContext
	extends PGPersistenceContext {
		
	private ValueExtractorFactory extractorFactory = new PagilaValueExtractorFactory(); 
	private ValueAssignerFactory valueAssignerFactory = new PagilaValueAssignerFactory();
	
	public PagilaPersistenceContext() {
		this(new PGImplementation());
	}
		
	public PagilaPersistenceContext(PGImplementation implementation) {
		super(implementation);
	}

	@Override
	public ValueAssignerFactory getValueAssignerFactory() {
		return valueAssignerFactory;
	}

	@Override
	public ValueExtractorFactory getValueExtractorFactory() {
		return this.extractorFactory;
	}
}
