/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

/**
 * Simple type name.
 * 
 * @author Topi Nieminen <topi.nieminen@gmail.com>
 */

public class SimpleTypeName	
	implements Type {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6588251307620934502L;
	
	private String typeName;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected SimpleTypeName() {
	}

	public SimpleTypeName(String type) {
		super();
		this.typeName = type;
	}

	@Override
	public String getTerminalSymbol() {
		return this.typeName;
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		v.end(this);
	}
	
	@Override
	public String toString() {		
		return this.typeName;
	}
}
