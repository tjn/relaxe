/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env;

import fi.tnie.db.ValueAssignerFactory;
import fi.tnie.db.ValueExtractorFactory;

public interface PersistenceContext<I extends Implementation<I>> {
	
	
	I getImplementation();
	
//	GeneratedKeyHandler generatedKeyHandler();	
	ValueExtractorFactory getValueExtractorFactory();			
	ValueAssignerFactory getValueAssignerFactory();

}
