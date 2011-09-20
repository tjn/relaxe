/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.expr.OrderBy;

public interface EntityQuerySortKey {
	int ordinal();
	OrderBy.SortKey sortKey();
}
