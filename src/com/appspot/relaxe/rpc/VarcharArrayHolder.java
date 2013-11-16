/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import com.appspot.relaxe.types.VarcharArrayType;
import com.appspot.relaxe.types.VarcharType;


public class VarcharArrayHolder
	extends StringArrayHolder<VarcharType, VarcharArrayType, VarcharArrayHolder> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2522909517688093544L;
	private static final String[] EMPTY = {};

	// TODO: fix null support
	public static final VarcharArrayHolder NULL_HOLDER = new VarcharArrayHolder();

	public VarcharArrayHolder() {
		super(EMPTY);
	}
	
	public VarcharArrayHolder(StringArray src) {
		super(src.getContent());
	}
	
	public VarcharArrayHolder(ArrayValue<String> src) {
		super(src);
	}

	public VarcharArrayHolder(String[] content) {
		super(content);
	}

	@Override
	public VarcharArrayHolder self() {
		return this;
	}

	@Override
	public VarcharArrayType getType() {
		return VarcharArrayType.TYPE;
	}
	
	@Override
	public VarcharArrayHolder asVarcharArrayHolder() {
		return this;
	}	
}
