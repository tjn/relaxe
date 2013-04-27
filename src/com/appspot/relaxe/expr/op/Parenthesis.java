/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.op;

import com.appspot.relaxe.expr.CompoundElement;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Expression;
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.VisitContext;

public class Parenthesis<E extends Expression>
	extends CompoundElement
	implements Expression {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1461089524289963961L;
	private E content;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected Parenthesis() {
	}
	
	public Parenthesis(E expression) {
		super();

		if (expression == null) {
			throw new NullPointerException("'content' must not be null");
		}
		
		this.content = expression;
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);		
		Symbol.PAREN_LEFT.traverse(vc, v);
		this.content.traverse(vc, v);
		Symbol.PAREN_RIGHT.traverse(vc, v);		
		v.end(this);
	}
	
	public E getContent() {
		return this.content;
	}
	
}
