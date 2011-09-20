/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.expr.ColumnReference;
import fi.tnie.db.expr.Predicate;

public interface EntityQueryTemplateAttribute {
	
	boolean isSelected(ColumnReference cref);	
	Predicate createPredicate(ColumnReference cref);
	EntityQuerySortKey createSortKey(ColumnReference cref);	
}
