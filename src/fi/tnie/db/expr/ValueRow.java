/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class ValueRow
	extends ElementList<ValueExpression> {

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		Symbol.PAREN_LEFT.traverse(vc, v);
		super.traverseContent(vc, v);
		Symbol.PAREN_RIGHT.traverse(vc, v);
	}
}
