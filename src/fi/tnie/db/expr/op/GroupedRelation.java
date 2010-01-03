/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.op;

import fi.tnie.db.expr.Subselect;

public class GroupedRelation
	extends Parenthesis<Subselect> {

	public GroupedRelation(Subselect expression) {
		super(expression);
	}
}
