/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.op;

import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.ValueExpression;

/**
 * Represents parenthesized value expression.
 *  
 * @author Administrator
 */

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

	@Override
	public ColumnName getColumnName() {		
		return getContent().getColumnName();
	}
	
}
