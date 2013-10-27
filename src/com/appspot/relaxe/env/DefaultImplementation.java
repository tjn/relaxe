/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env;

import java.util.Properties;

import com.appspot.relaxe.DefaultValueAssignerFactory;
import com.appspot.relaxe.DefaultValueExtractorFactory;
import com.appspot.relaxe.ValueAssignerFactory;
import com.appspot.relaxe.ValueExtractorFactory;

public abstract class DefaultImplementation<I extends Implementation<I>>	
	implements Implementation<I> {

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
	
	@Override
	public Properties getDefaultProperties() {
		return new Properties();
	}	
}
