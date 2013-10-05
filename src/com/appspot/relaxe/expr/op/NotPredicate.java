/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.op;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.VisitContext;

public class NotPredicate
	implements Predicate {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 2494775816356406506L;
	private Predicate inner;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected NotPredicate() {
	}
	
	public NotPredicate(Predicate inner) {
		super();		
		
		if (inner == null) {
			throw new NullPointerException("'inner' must not be null");
		}
		
		this.inner = inner;
	}

	@Override
	public String getTerminalSymbol() {
		return null;
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		vc = v.start(vc, this);
		SQLKeyword.NOT.traverse(vc, v);
		inner.traverse(vc, v);		
	}	
}
