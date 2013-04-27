/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

public abstract class CompoundElement implements Element {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7299007466010084749L;

	@Override
	public final String getTerminalSymbol() {
		return null;
	}
		
	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		try {
			vc = v.start(vc, this);
			traverseContent(vc, v);
		}
		finally {
			v.end(this);
		}		
	}
	
	protected void traverseContent(VisitContext vc, ElementVisitor v) {		
	}
	
	protected void traverseNonEmpty(Element e, VisitContext vc, ElementVisitor v) {		
		if (e != null) {
			v.start(vc, this);
			e.traverse(vc, v);
			v.end(this);
		}
	}
		
	public String generate() {
		StringBuffer dest = new StringBuffer();
		ElementVisitor v = new QueryGenerator(dest);
		traverse(null, v);		
		return dest.toString();			
	}
}
