/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.op;

import fi.tnie.db.expr.TableExpression;

public class GroupedRelation
	extends Parenthesis<TableExpression> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1172889989852534036L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected GroupedRelation() {
	}

	public GroupedRelation(TableExpression expression) {
		super(expression);
	}
}
