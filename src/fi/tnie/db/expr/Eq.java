package fi.tnie.db.expr;

import fi.tnie.db.QueryContext;

public class Eq
	implements Predicate {

	private ColumnExpr a;
	private ColumnExpr b;
	
	public Eq(ColumnExpr a, ColumnExpr b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public void generate(QueryContext qc, StringBuffer dest) {
		dest.append("(");
		a.generate(qc, dest);
		dest.append("=");
		b.generate(qc, dest);
		dest.append(") ");		
	}
	

}
