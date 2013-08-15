/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

public abstract class AbstractClause extends CompoundElement {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5454554593152361247L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AbstractClause() {
	}

	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		Element c = getContent();
		
		if (c != null) {			
			traverseClause(vc, v);			
			c.traverse(vc, v);			
		}
	}
	
	protected abstract void traverseClause(VisitContext vc, ElementVisitor v);

	protected abstract Element getContent();
}
