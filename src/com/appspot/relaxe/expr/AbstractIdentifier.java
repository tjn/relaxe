/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;


public abstract class AbstractIdentifier
	extends SimpleElement
	implements Identifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2304652443656237941L;
	private String name;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AbstractIdentifier() {
	}
	
	protected AbstractIdentifier(String name) {
		this.name = name;
	}
	
	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		v.end(this);
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public abstract String getTerminalSymbol();	

}