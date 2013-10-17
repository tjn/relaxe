/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.pg;

import com.appspot.relaxe.ValueExtractorFactory;
import com.appspot.relaxe.env.AbstractGeneratedKeyHandler;

public class PGGeneratedKeyHandler 
	extends AbstractGeneratedKeyHandler {
	
	private ValueExtractorFactory extractorFactory;

	public PGGeneratedKeyHandler(ValueExtractorFactory extractorFactory) {
		super();
		this.extractorFactory = extractorFactory;
	}
	
	@Override
	protected ValueExtractorFactory getValueExtractorFactory() {
		return this.extractorFactory;
	}
}