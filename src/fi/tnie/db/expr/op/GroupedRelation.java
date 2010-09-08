/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.op;

import fi.tnie.db.expr.TableExpression;

public class GroupedRelation
	extends Parenthesis<TableExpression> {

	public GroupedRelation(TableExpression expression) {
		super(expression);
	}
}
