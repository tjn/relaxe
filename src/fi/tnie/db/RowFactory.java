/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.expr.SelectQuery;


public interface RowFactory<R extends Row> {	
	R newInstance(InstantiationContext ic) 
		throws InstantiationException, IllegalAccessException;

	public InstantiationContext prepare(SelectQuery query);
	void finish(InstantiationContext ic);
	
	
}
