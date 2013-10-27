/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

public class DeleteStatement
	extends SQLDataChangeStatement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7983755125216586175L;

	private TableReference target;	
	private Where where;
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected DeleteStatement() {
	}
	
	public DeleteStatement(TableReference tableReference, Predicate p) {
		super(Name.DELETE);
		
		if (tableReference == null) {
			throw new NullPointerException("tableReference");
		}
		
		this.target = tableReference;
		this.where = (p == null) ? null : new Where(p);
	}
		
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		SQLKeyword.DELETE.traverse(vc, v);		
		SQLKeyword.FROM.traverse(vc, v);
		getTarget().traverse(vc, v);		
		
		if (getWhere() != null) {
			getWhere().traverse(vc, v);
		}
	}

	public TableReference getTarget() {
		return target;
	}	

	public Where getWhere() {
		return where;
	}
}
