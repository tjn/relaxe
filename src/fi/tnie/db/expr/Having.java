/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class Having extends AbstractClause {
	
	public Having() {
		super(Keyword.HAVING);
	}

	private Predicate searchCondition;
	
//	@Override
//	public void generate(SimpleQueryContext qc, StringBuffer dest) {
//		Predicate p = getSearchCondition();
//		
//		if (p != null) {
//			dest.append("HAVING ");
//			p.generate(qc, dest);			
//		}
//	}

	public void setSearchCondition(Predicate predicate) {
		this.searchCondition = predicate;
	}

	public Predicate getSearchCondition() {
		return searchCondition;
	}
	
	@Override
	protected Element getContent() {
		return getSearchCondition();
	}
}
