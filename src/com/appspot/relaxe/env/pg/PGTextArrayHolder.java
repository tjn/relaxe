/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.pg;

import com.appspot.relaxe.rpc.ArrayValue;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.rpc.StringArrayHolder;
import com.appspot.relaxe.types.VarcharType;

public class PGTextArrayHolder
	extends StringArrayHolder<VarcharType, PGTextArrayType, PGTextArrayHolder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1432615933967175087L;
	
	public static final PGTextArrayHolder NULL_HOLDER = new PGTextArrayHolder();
	
	public PGTextArrayHolder() {
		super();
	}

	public PGTextArrayHolder(String[] data) {
		super(data);	
	}
	
	public PGTextArrayHolder(ArrayValue<String> value) {
		super(value);
	}

	@Override
	public PGTextArrayHolder self() {
		return this;
	}

	@Override
	public PGTextArrayType getType() {
		return PGTextArrayType.TYPE;
	}

	public static PGTextArrayHolder newHolder(ArrayValue<String> newValue) {
		return new PGTextArrayHolder(newValue);
	}

	public static PGTextArrayHolder of(PrimitiveHolder<?, ?, ?> holder) {
		Object h = holder.self();
		PGTextArrayHolder mh = (PGTextArrayHolder) h;	
		return mh;
	}
}
