/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.pg;

import fi.tnie.db.expr.AbstractClause;
import fi.tnie.db.expr.Element;
import fi.tnie.db.expr.ValueElement;

public class ReturningClause
	extends AbstractClause {


	private ValueElement e;

	public ReturningClause() {
		super(PostgreSQLKeyword.RETURNING);
	}

	@Override
	protected Element getContent() {

		return null;
	}

}
