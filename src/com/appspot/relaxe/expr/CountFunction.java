/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import com.appspot.relaxe.types.PrimitiveType;

public class CountFunction
	extends CompoundElement
	implements ValueExpression {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3120742542013048386L;
	
	private static final Identifier NAME = new OrdinaryIdentifier("COUNT");
	
	private boolean distinct;
		
	public CountFunction() {
		this(false);
	}

	public CountFunction(boolean distinct) {
		super();
		this.distinct = distinct;
	}

	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		Identifier n = NAME;
		n.traverse(vc, v);
		Symbol.PAREN_LEFT.traverse(vc, v);
		
		if (distinct) {
			SQLKeyword.DISTINCT.traverse(vc, v);
		}
		
		Symbol.ASTERISK.traverse(vc, v);
		Symbol.PAREN_RIGHT.traverse(vc, v);		
	}

	@Override
	public Identifier getColumnName() {
		return null;
	}

	@Override
	public int getType() {
		return PrimitiveType.BIGINT;
	}
	
}
