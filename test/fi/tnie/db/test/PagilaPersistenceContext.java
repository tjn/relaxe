/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.test;

import fi.tnie.db.ValueAssignerFactory;
import fi.tnie.db.ValueExtractorFactory;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.pg.PGImplementation;

public class PagilaPersistenceContext
	implements PersistenceContext<PGImplementation> {
	
	
	private PGImplementation implementation;
		
	private ValueExtractorFactory extractorFactory = new PagilaValueExtractorFactory(); 
	private ValueAssignerFactory valueAssignerFactory = new PagilaValueAssignerFactory();
	
	
	public PagilaPersistenceContext(PGImplementation implementation) {
		super();
		
		if (implementation == null) {
			throw new NullPointerException("implementation");
		}
		
		this.implementation = implementation;
	}

	@Override
	public ValueAssignerFactory getValueAssignerFactory() {
		return valueAssignerFactory;
	}

	@Override
	public ValueExtractorFactory getValueExtractorFactory() {
		return this.extractorFactory;
	}

	@Override
	public PGImplementation getImplementation() {
		return this.implementation;
	}

}
