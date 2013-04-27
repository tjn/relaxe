/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env;

import com.appspot.relaxe.ValueExtractorFactory;

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
