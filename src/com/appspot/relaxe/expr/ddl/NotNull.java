/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.VisitContext;

public class NotNull	 
    implements ColumnConstraint {
	
	public static final ColumnConstraint NOT_NULL = new NotNull();
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private NotNull() {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2690177949253834868L;

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		SQLKeyword.NOT.traverse(vc, v);
		SQLKeyword.NULL.traverse(vc, v);
		v.end(this);
	}

	@Override
	public String getTerminalSymbol() {
		return null;
	}

}
