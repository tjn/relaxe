/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;

import fi.tnie.db.expr.ColumnReference;
import fi.tnie.db.expr.OrderBy;

public interface EntityQuerySortKey<A extends Attribute>
	extends Serializable {
	
	OrderBy.SortKey sortKey(ColumnReference cr);	
	A attribute();
}
