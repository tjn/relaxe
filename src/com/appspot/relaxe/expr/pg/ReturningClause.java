/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.pg;

import com.appspot.relaxe.expr.AbstractClause;
import com.appspot.relaxe.expr.Element;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.ValueElement;
import com.appspot.relaxe.expr.VisitContext;

public class ReturningClause
	extends AbstractClause {

	/**
	 * 
	 */
	private static final long serialVersionUID = 613528021390717351L;
	private ValueElement e;

	public ReturningClause() {
		super();
	}

	@Override
	protected Element getContent() {
		return null;
	}

	@Override
	protected void traverseClause(VisitContext vc, ElementVisitor v) {
		PostgreSQLKeyword.RETURNING.traverse(vc, v);
	}
	
}
