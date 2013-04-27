/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.op;

import com.appspot.relaxe.expr.CompoundElement;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.expr.VisitContext;

public class IsNull
	extends CompoundElement
	implements Predicate {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3352484016217751225L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected IsNull() {
	}
	
	private ValueExpression expression;	
	
	public IsNull(ValueExpression e) {
		this.expression = e;
	}	

	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		vc = v.start(vc, this);					
		expression.traverse(vc, v);
		SQLKeyword.IS.traverse(vc, v);
		SQLKeyword.NULL.traverse(vc, v);
		v.end(this);
	}
	
	@Override
	public Predicate parenthesize() {
		return new ParenthesizedPredicate(this);
	}
}
