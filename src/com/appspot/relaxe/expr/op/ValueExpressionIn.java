/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.op;

import java.util.Collection;

import com.appspot.relaxe.expr.CompoundElement;
import com.appspot.relaxe.expr.ElementList;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.expr.VisitContext;


/**
 * TODO
 * 
 */
public class ValueExpressionIn
	extends CompoundElement
	implements Predicate {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4384506983416435234L;
	private ValueExpression left;
	private ElementList<ValueExpression> elements;
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected ValueExpressionIn() {
	}
	
	public ValueExpressionIn(ValueExpression left, Collection<ValueExpression> values) {
		super();
		this.left = left;
				
		this.elements = new ElementList<ValueExpression>(Symbol.COMMA);
		
		for (ValueExpression e : values) {
			this.elements.add(e);
		}
	}
	
	
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		left.traverse(vc, v);				
		SQLKeyword.IN.traverse(vc, v);
		Symbol.PAREN_LEFT.traverse(vc, v);
		this.elements.traverse(vc, v);
		Symbol.PAREN_RIGHT.traverse(vc, v);		
	}
	
	@Override
	public Predicate parenthesize() {
		return null;
	}
	
	
	
}
