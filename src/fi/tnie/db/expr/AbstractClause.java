/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public abstract class AbstractClause extends CompoundElement {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5454554593152361247L;
	
	private Keyword clause;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AbstractClause() {
	}
	
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
