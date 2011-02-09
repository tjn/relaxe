/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env;

import fi.tnie.db.DefaultValueAssignerFactory;
import fi.tnie.db.DefaultValueExtractorFactory;
import fi.tnie.db.ValueAssignerFactory;
import fi.tnie.db.ValueExtractorFactory;
import fi.tnie.db.meta.impl.DefaultEnvironment;

public abstract class DefaultImplementation
	extends DefaultEnvironment
	implements Implementation {

	private ValueExtractorFactory valueExtractorFactory; 
	private ValueAssignerFactory valueAssignerFactory;
	
	@Override
	public abstract CatalogFactory catalogFactory();

	@Override
	public ValueExtractorFactory getValueExtractorFactory() {
		if (valueExtractorFactory == null) {			
			valueExtractorFactory = createValueExtractorFactory();			
		}

		return valueExtractorFactory;
	}
	
	protected ValueExtractorFactory createValueExtractorFactory() {
		return new DefaultValueExtractorFactory();
	}
	
	@Override
	public ValueAssignerFactory getValueAssignerFactory() {
		if (valueAssignerFactory == null) {
			this.valueAssignerFactory = createValueAssignerFactory();			
		}

		return valueAssignerFactory;
	}

	protected ValueAssignerFactory createValueAssignerFactory() {
		return new DefaultValueAssignerFactory();
	}
}
