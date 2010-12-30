/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public interface Change<V> {			
		V from();
		V to();	
}
