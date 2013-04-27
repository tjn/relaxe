/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import com.appspot.relaxe.expr.ColumnReference;
import com.appspot.relaxe.expr.Predicate;

public class DefaultTemplateAttribute
	implements EntityQueryTemplateAttribute {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7808021636368072149L;
			
	public static DefaultTemplateAttribute get() {
		return new DefaultTemplateAttribute();
	}
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	public DefaultTemplateAttribute() {
	}


	@Override
	public Predicate createPredicate(ColumnReference cref) {
		return null;
	}

	@Override
	public boolean isSelected(ColumnReference cref) {
		return true;
	}
	
//	@Override
//	public EntityQuerySortKey asSortKey() {
//		return null;
//	} 
}
