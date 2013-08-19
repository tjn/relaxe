/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;


public class OrdinaryIdentifier
	extends AbstractIdentifier
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8379767503417798479L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private OrdinaryIdentifier() {
	}
	
	OrdinaryIdentifier(String name) {
		super(name);		
	}

	@Override
	public boolean isOrdinary() {	
		return true;
	}

	@Override
	public String toString() {		
		return "[" + getName() + ": " + super.toString() + "]";
	}

	@Override
	public String getTerminalSymbol() {
		return getName();
	}
	
	@Override
	public boolean isDelimited() {
		return false;
	}
}
