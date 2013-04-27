/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.ValueAssignerFactory;
import com.appspot.relaxe.ValueExtractorFactory;
import com.appspot.relaxe.env.pg.PGImplementation;
import com.appspot.relaxe.env.pg.PGPersistenceContext;

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
