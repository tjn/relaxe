/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.meta.ForeignKey;

public interface EntityQueryContext {

	EntityQuery<?, ?, ?, ?, ?, ?, ?, ?, ?> getQuery();
	
	TableReference getTableRef(EntityQueryElementTag tag); 

	public TableReference getReferenced(TableReference referencing, ForeignKey fk)
		throws EntityException;
	
	/**
	 * Returns the table reference the value in the <code>column</code> originated from. 
	 * 
	 * @param column
	 * @return
	 */
	TableReference getOrigin(int column);
}
