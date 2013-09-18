/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.query;

import com.appspot.relaxe.ent.EntityQueryContext;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.expr.ColumnReference;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.meta.Column;

public class EntityQueryAttributeValueReference<
	A extends com.appspot.relaxe.ent.Attribute,
	QE extends EntityQueryElement<A, ?, ?, ?, ?, ?, ?, ?, QE>
>
	implements EntityQueryValueReference {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8919013660144056879L;
	
	private QE element;
	private A attribute;
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private EntityQueryAttributeValueReference() {
	}
		
	public EntityQueryAttributeValueReference(QE element, A attribute) {
		super();
		
		if (element == null) {
			throw new NullPointerException("element");
		}
		
		if (attribute == null) {
			throw new NullPointerException("attribute");
		}
		
		this.element = element;
		this.attribute = attribute;
		
	}
	
	@Override
	public ValueExpression expression(EntityQueryContext c) {
		TableReference tref = c.getTableRef(element);
		
		if (tref == null) {
			throw new NullPointerException();
		}
		
		Column column = this.element.getMetaData().getColumn(this.attribute);
		ColumnReference ref = new ColumnReference(tref, column);		
		
		return ref;
	}	
}
