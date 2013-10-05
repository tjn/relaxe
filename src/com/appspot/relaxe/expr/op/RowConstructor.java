/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.op;

import java.util.Collection;

import com.appspot.relaxe.expr.CompoundElement;
import com.appspot.relaxe.expr.ElementList;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.expr.VisitContext;

public class RowConstructor
	extends CompoundElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2415272956499443256L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private RowConstructor() {
	}
	
	private ElementList<ValueExpression> elements;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected RowConstructor(ElementList<ValueExpression> elements) {
		this.elements = elements;
	}
	
	public RowConstructor(Collection<ValueExpression> values) {
		super();						
		this.elements = new ElementList<ValueExpression>(Symbol.COMMA, values);
	}
	
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		this.elements.traverse(vc, v);
	}
	
	
	
	
}
