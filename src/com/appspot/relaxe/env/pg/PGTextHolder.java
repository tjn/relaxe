/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.pg;

import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.rpc.StringHolder;

public class PGTextHolder
	extends StringHolder<PGTextType, PGTextHolder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2479889538730689743L;
	public static final PGTextHolder NULL_HOLDER = new PGTextHolder();
	public static final PGTextHolder EMPTY_HOLDER = new PGTextHolder("");
	
	/**
	 * TODO: should we have size?
	 */
		
	protected PGTextHolder() {
		super();
	}

	public PGTextHolder(String value) {
		super(value);
	}
	
	public static PGTextHolder valueOf(String s) {
		return 
			(s == null) ? NULL_HOLDER : 
			(s.equals("")) ? EMPTY_HOLDER :
			new PGTextHolder(s);
	}
	
	public static String toString(PGTextHolder h) {
		return (h == null) ? null : h.value();
	}
	
	@Override
	public PGTextType getType() {
		return PGTextType.TYPE;
	}
	
	@Override
	public PGTextHolder asStringHolder() {		
		return this;
	}

	@Override
	public PGTextHolder self() {
		return this;
	}
	
	public static PGTextHolder of(PrimitiveHolder<?, ?, ?> holder) {
		Object s = holder.self();
				
		PGTextHolder h = (PGTextHolder) s;	
		return h;
	}
}
