/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import com.appspot.relaxe.types.VarcharType;

public class VarcharHolder
	extends StringHolder<VarcharType, VarcharHolder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2479889538730689743L;
	public static final VarcharHolder NULL_HOLDER = new VarcharHolder();
	public static final VarcharHolder EMPTY_HOLDER = new VarcharHolder("");
	
	/**
	 * TODO: should we have size?
	 */
		
	protected VarcharHolder() {
		super();
	}

	public VarcharHolder(String value) {
		super(value);
	}
	
	public static VarcharHolder valueOf(String s) {
		return 
			(s == null) ? NULL_HOLDER : 
			(s.equals("")) ? EMPTY_HOLDER :
			new VarcharHolder(s);
	}
	
	public static String toString(VarcharHolder h) {
		return (h == null) ? null : h.value();
	}
	
	@Override
	public VarcharType getType() {
		return VarcharType.TYPE;
	}
	
//	@Override
//	public int getSqlType() {
//		return AbstractPrimitiveType.VARCHAR;
//	}
	
	@Override
	public VarcharHolder asVarcharHolder() {	
		return this;
	}

	@Override
	public VarcharHolder self() {
		return this;
	}
	
	public static VarcharHolder of(PrimitiveHolder<?, ?, ?> holder) {
		return holder.asVarcharHolder();
	}
}
