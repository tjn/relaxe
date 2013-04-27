/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import java.io.Serializable;

import com.appspot.relaxe.expr.ColumnReference;
import com.appspot.relaxe.expr.Predicate;


public interface EntityQueryTemplateAttribute
	extends Serializable {
			
	boolean isSelected(ColumnReference cref);	
	Predicate createPredicate(ColumnReference cref);	
//	EntityQuerySortKey asSortKey();
}
