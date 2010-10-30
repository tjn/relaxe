/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env;

import fi.tnie.db.DefaultValueExtractorFactory;
import fi.tnie.db.ValueExtractorFactory;
import fi.tnie.db.meta.impl.DefaultEnvironment;

public abstract class DefaultImplementation
	extends DefaultEnvironment
	implements Implementation {

	private DefaultValueExtractorFactory valueExtractorFactory; 
	
	@Override
	public abstract CatalogFactory catalogFactory();
		
	public ValueExtractorFactory getValueExtractorFactory() {
		if (valueExtractorFactory == null) {
			valueExtractorFactory = new DefaultValueExtractorFactory();			
		}

		return valueExtractorFactory;
	}
}
