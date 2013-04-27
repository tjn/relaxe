/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import java.io.Serializable;

import com.appspot.relaxe.expr.ColumnReference;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.TableReference;


public interface EntityQueryPredicate<A extends Attribute>
	extends Serializable {
	
	Predicate predicate(TableReference tref, ColumnReference cr);
	
	/**
	 * May return <code>null</code> if this template does not require a column reference to be evaluated. 
	 *   
	 * @return
	 */
	A attribute();
}
