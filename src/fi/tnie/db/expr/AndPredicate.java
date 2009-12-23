package fi.tnie.db.expr;

import fi.tnie.db.QueryContext;

public class AndPredicate
	implements Predicate {
	
	private Predicate left;
	private Predicate right;

	public AndPredicate(Predicate left, Predicate right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public void generate(QueryContext qc, StringBuffer dest) {
		dest.append(" (");
		left.generate(qc, dest);
		dest.append(" AND ");
		right.generate(qc, dest);
		dest.append(") ");
	}	
	
	public static Predicate newAnd(Predicate a, Predicate b) {
		if (a == null) {
			return b;
		}
		if (b == null) {
			return a;
		}
		
		return new AndPredicate(a, b);
	}
}
