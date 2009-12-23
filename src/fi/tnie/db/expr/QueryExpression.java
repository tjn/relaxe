package fi.tnie.db.expr;

import fi.tnie.db.QueryContext;

public interface QueryExpression {
	public void generate(QueryContext qc, StringBuffer dest);
}
