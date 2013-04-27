/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env;

import com.appspot.relaxe.ValueAssignerFactory;
import com.appspot.relaxe.ValueExtractorFactory;

public interface PersistenceContext<I extends Implementation<I>> {
	
	I getImplementation();	
	GeneratedKeyHandler generatedKeyHandler();	
	ValueExtractorFactory getValueExtractorFactory();			
	ValueAssignerFactory getValueAssignerFactory();

}
