/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.op;

import java.util.List;

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

	@Override
	public int getColumnCount() {		
		return getContent().getColumnCount();
	}

	@Override
	public List<? extends ColumnName> getColumnNames() {
		return getContent().getColumnNames();
	}
	
	
	@Override
	public ValueExpression getColumnExpr(int column) {
		if (column != 1) {
			throw new IndexOutOfBoundsException(Integer.toString(column));
		}
	
		return this;
	}
}
