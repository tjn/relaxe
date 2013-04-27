/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.op;

import com.appspot.relaxe.expr.ColumnName;
import com.appspot.relaxe.expr.ValueExpression;

/**
 * Represents parenthesized value expression.
 *  
 * @author Administrator
 */

public class GroupExpression
	extends Parenthesis<ValueExpression>
	implements ValueExpression
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2831127865632365200L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected GroupExpression() {
	}
	
	public GroupExpression(ValueExpression inner) {
		super(inner);		
	}

	@Override
	public int getType() {
		return getContent().getType();
	}

	@Override
	public ColumnName getColumnName() {		
		return getContent().getColumnName();
	}
	
}
