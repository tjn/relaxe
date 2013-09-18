/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.query;

import com.appspot.relaxe.ent.EntityQueryContext;
import com.appspot.relaxe.expr.ValueExpression;

public class EntityQueryValueExpression
	implements EntityQueryValueReference {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9013982662563402944L;
	
	private ValueExpression value;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private EntityQueryValueExpression() {
	}

	public EntityQueryValueExpression(ValueExpression value) {
		super();
		
		if (value == null) {
			throw new NullPointerException("value");
		}
		
		this.value = value;
	}


	@Override
	public ValueExpression expression(EntityQueryContext c) {
		return this.value;
	}
}
