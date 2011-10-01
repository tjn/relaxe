/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public abstract class SimpleClause
	extends AbstractClause {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3193883178079657128L;
	private Element content;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected SimpleClause() {
	}

	public SimpleClause(SQLKeyword clause, Element clauseContent) {
		super(clause);
		
		if (clauseContent == null) {
			throw new NullPointerException("content");
		}
		
		this.content = clauseContent;
	}	

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		getClause().traverse(vc, v);
		getContent().traverse(vc, v);						
		v.end(this);		
	}

	@Override
	protected Element getContent() {
		return this.content;
	}
}
