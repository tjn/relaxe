/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

/**
 * Top-level SELECT -statement
 * @author Administrator
 */

public abstract class Query 	
	extends CompoundElement {
		
	public String generate() {
		StringBuffer dest = new StringBuffer();
		ElementVisitor v = new QueryGenerator(dest);
		traverse(null, v);		
		return dest.toString();		
	}	
}
