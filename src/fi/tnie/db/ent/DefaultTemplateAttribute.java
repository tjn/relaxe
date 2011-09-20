/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.expr.ColumnReference;
import fi.tnie.db.expr.Predicate;

public class DefaultTemplateAttribute
	implements EntityQueryTemplateAttribute {
	
	public static EntityQueryTemplateAttribute instance;
	
	public static EntityQueryTemplateAttribute getInstance() {
		if (instance == null) {
			instance = new DefaultTemplateAttribute();		
		}

		return instance;
	}

	@Override
	public Predicate createPredicate(ColumnReference cref) {
		return null;
	}

	@Override
	public EntityQuerySortKey createSortKey(ColumnReference cref) {
		return null;
	}

	@Override
	public boolean isSelected(ColumnReference cref) {
		return true;
	}
}
