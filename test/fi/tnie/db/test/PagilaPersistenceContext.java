/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.test;

import fi.tnie.db.ValueAssignerFactory;
import fi.tnie.db.ValueExtractorFactory;
import fi.tnie.db.env.PersistenceContext;

public class PagilaPersistenceContext
	implements PersistenceContext {
		
	private ValueExtractorFactory extractorFactory = new PagilaValueExtractorFactory(); 
	private ValueAssignerFactory valueAssignerFactory = new PagilaValueAssignerFactory(); 

	@Override
	public ValueAssignerFactory getValueAssignerFactory() {
		return valueAssignerFactory;
	}

	@Override
	public ValueExtractorFactory getValueExtractorFactory() {
		return this.extractorFactory;
	}

}
