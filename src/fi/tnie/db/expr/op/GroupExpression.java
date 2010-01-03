/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.op;

import fi.tnie.db.expr.ValueExpression;

public class GroupExpression
	extends Parenthesis<ValueExpression>
	implements ValueExpression
{
	public GroupExpression(ValueExpression inner) {
		super(inner);		
	}

	@Override
	public int getType() {
		return getContent().getType();
	}	
}
