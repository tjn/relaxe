/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.hsqldb.expr;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.expr.VisitContext;

public class Shutdown
	extends Statement {
	
	public static final Shutdown STATEMENT = new Shutdown();	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4505440690619367270L;	
	
	public Shutdown() {
		super();
	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {

		HSQLDBKeyword.SHUTDOWN.traverse(vc, v);
	}

	


}
