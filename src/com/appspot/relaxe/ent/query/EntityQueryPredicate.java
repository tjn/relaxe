/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.query;

import java.io.Serializable;

import com.appspot.relaxe.ent.EntityQueryContext;
import com.appspot.relaxe.expr.Predicate;

public interface EntityQueryPredicate
	extends Serializable {
	
	Predicate predicate(EntityQueryContext ctx);
}
