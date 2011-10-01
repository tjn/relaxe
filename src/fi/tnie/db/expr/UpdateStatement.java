/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class UpdateStatement
	extends Statement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8042322606478948245L;
	private TableReference target;			
	private ElementList<Assignment> assignmentClause;
	private Where where;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected UpdateStatement() {
	}
				
	public UpdateStatement(TableReference tref, ElementList<Assignment> assignmentClause, Predicate p) {
		super(Name.UPDATE);
		
		if (tref == null) {
			throw new NullPointerException("'tref' must not be null");
		}
		
		if (assignmentClause == null) {
			throw new NullPointerException("'assignmentClause' must not be null");
		}		
		
		this.target = tref;
		this.assignmentClause = assignmentClause;
		
		if (p != null) {
			getWhere().setSearchCondition(p);
		}
	}	
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		SQLKeyword.UPDATE.traverse(vc, v);		
		getTarget().traverse(vc, v);		
		SQLKeyword.SET.traverse(vc, v);
		this.assignmentClause.traverse(vc, v);
		
		final Where w = this.where;
				
		if (w != null && w.getContent() != null) {
			w.traverse(vc, v);
		}
	}

	public TableReference getTarget() {
		return target;
	}	
	
	public ElementList<Assignment> getAssignmentList() {
		if (assignmentClause == null) {
			assignmentClause = new ElementList<Assignment>();			
		}

		return assignmentClause;
	}

	
	public Where getWhere() {
		if (this.where == null) {
			this.where = new Where();			
		}

		return this.where;
	}
}
