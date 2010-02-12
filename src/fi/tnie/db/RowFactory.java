/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;


public interface RowFactory<R extends Row> {	

//	Table getSource();
//	
	R newInstance(InstantiationContext ic) 
		throws InstantiationException, IllegalAccessException;

//	RowQuery<C, R> createRowQuery();
//	Class<C> getColumnNameType();	
//	Column getColumn(C c);	
//	Set<C> getPKDefinition();
	
}
