/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env;

import fi.tnie.db.ValueExtractorFactory;

public class DefaultGeneratedKeyHandler
	extends AbstractGeneratedKeyHandler {
	
	private ValueExtractorFactory valueExtractorFactory;
	
	public DefaultGeneratedKeyHandler(ValueExtractorFactory valueExtractorFactory) {
		super();
		this.valueExtractorFactory = valueExtractorFactory;
	}

	@Override
	protected ValueExtractorFactory getValueExtractorFactory() {
		return this.valueExtractorFactory;
	}
}
