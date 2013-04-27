/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import com.appspot.relaxe.expr.TableReference;

public interface EntityQueryPredicateContext {

	
	TableReference getTableReference(EntityQueryPredicate<?> p); 
	
}
