/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import java.io.Serializable;

import com.appspot.relaxe.expr.ColumnReference;
import com.appspot.relaxe.expr.OrderBy;


public interface EntityQuerySortKey<A extends Attribute>
	extends Serializable {
	
	OrderBy.SortKey sortKey(ColumnReference cr);	
	A attribute();
}
