/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.op;

import com.appspot.relaxe.expr.CompoundElement;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.VisitContext;

public class Exists
	extends CompoundElement
	implements Predicate {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7282243674920197042L;
	private QueryExpression query;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected Exists() {
	}

	public Exists(QueryExpression query) {
		super();
		
		if (query == null) {
			throw new NullPointerException("'query' must not be null");
		}
		
		this.query = query;
	}

	//	@Override
//	public void generate(SimpleQueryContext qc, StringBuffer dest) {
//		dest.append("EXISTS (");
//		fullSelect.generate(qc, dest);
//		dest.append(") ");
//	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		vc = v.start(vc, this);
		
		SQLKeyword.EXISTS.traverse(vc, v);		
		Symbol.PAREN_LEFT.traverse(vc, v);
		this.query.traverse(vc, v);
		Symbol.PAREN_RIGHT.traverse(vc, v);
		
		v.end(this);
	}
	
	@Override
	public Predicate parenthesize() {
		return new ParenthesizedPredicate(this);
	}
}
