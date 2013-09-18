/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.query;

import com.appspot.relaxe.ent.EntityQueryContext;
import com.appspot.relaxe.ent.EntityQueryElementTag;
import com.appspot.relaxe.expr.ColumnReference;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.meta.Column;

public class EntityQueryColumnReference
	implements EntityQueryValueReference {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8919013660144056879L;
	
	private EntityQueryElementTag element;
	private Identifier columnName;
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private EntityQueryColumnReference() {
	}
		
	public EntityQueryColumnReference(EntityQueryElementTag element, Identifier columnName) {
		super();
		
		if (element == null) {
			throw new NullPointerException("element");
		}
		
		if (columnName == null) {
			throw new NullPointerException("columnName");
		}
		
		this.element = element;
		this.columnName = columnName;
		
	}
	
	@Override
	public ValueExpression expression(EntityQueryContext c) {
		TableReference tref = c.getTableRef(element);
		
		Column column = tref.getTable().columnMap().get(columnName);
		
		if (column == null) {
			throw new NullPointerException("no column " + columnName.toString() + " in table " + tref.getTable().getQualifiedName());
		}
		
		ColumnReference ref = new ColumnReference(tref, column);		
		
		return ref;
	}	
}
