/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.pg;

import com.appspot.relaxe.expr.AbstractClause;
import com.appspot.relaxe.expr.Element;
import com.appspot.relaxe.expr.ValueElement;

public class ReturningClause
	extends AbstractClause {

	/**
	 * 
	 */
	private static final long serialVersionUID = 613528021390717351L;
	private ValueElement e;

	public ReturningClause() {
		super(PostgreSQLKeyword.RETURNING);
	}

	@Override
	protected Element getContent() {
		return null;
	}

}
