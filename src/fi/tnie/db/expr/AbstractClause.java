/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public abstract class AbstractClause extends CompoundElement {
	
	private Keyword clause;
	
	public AbstractClause(Keyword clause) {
		super();
		
		if (clause == null) {
			throw new NullPointerException("'clause' must not be null");
		} 
		
		this.clause = clause;
	}

	public Keyword getClause() {
		return clause;
	}

	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		Element c = getContent();
		
		if (c != null) {
			getClause().traverse(vc, v);
			c.traverse(vc, v);			
		}
	}
	
	protected abstract Element getContent();
}
