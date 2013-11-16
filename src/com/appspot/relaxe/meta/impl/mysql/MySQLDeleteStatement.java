/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.mysql;

import com.appspot.relaxe.expr.DeleteStatement;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.OrdinaryIdentifier;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.expr.VisitContext;

public class MySQLDeleteStatement
	extends DeleteStatement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2492700739356278591L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected MySQLDeleteStatement() {
	}
	
	public MySQLDeleteStatement(TableReference tref, Predicate p) {
		super(tref, p);		
	}		
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {		
		// DELETE FROM R USING <schema>.<table> R WHERE ...
		
		SQLKeyword.DELETE.traverse(vc, v);		
		SQLKeyword.FROM.traverse(vc, v);		
		TableReference tref = getTarget();
		OrdinaryIdentifier cn = tref.getCorrelationName(v.getContext());
		cn.traverse(vc, v);		
		MySQLKeyword.USING.traverse(vc, v);				
		tref.traverse(vc, v);	
		getWhere().traverse(vc, v);
	}
}