/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

public class ValueRow
	extends ElementList<ValuesListElement> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4622202673145576700L;

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		Symbol.PAREN_LEFT.traverse(vc, v);
		super.traverseContent(vc, v);
		Symbol.PAREN_RIGHT.traverse(vc, v);
	}
}
