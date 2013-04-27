/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import com.appspot.relaxe.types.BooleanType;
import com.appspot.relaxe.types.PrimitiveType;


public class BooleanHolder
	extends PrimitiveHolder<Boolean, BooleanType, BooleanHolder> {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = -7895663698067736815L;
	private Boolean value;	
	public static final BooleanHolder NULL_HOLDER = new BooleanHolder();
	public static final BooleanHolder TRUE_HOLDER = new BooleanHolder(true);
	public static final BooleanHolder FALSE_HOLDER = new BooleanHolder(false);
		
	public static BooleanHolder valueOf(boolean v) {
		return v ? TRUE_HOLDER : FALSE_HOLDER;
	}
	
	public static BooleanHolder valueOf(Boolean v) {
		return (v == null) ? NULL_HOLDER : valueOf(v.booleanValue());
	}
			
	public BooleanHolder(boolean value) {
		this.value = Boolean.valueOf(value);
	}
	
	protected BooleanHolder() {		
	}
	
	@Override
	public Boolean value() {
		return this.value;
	}

	@Override
	public BooleanType getType() {
		return BooleanType.TYPE;
	}

	@Override
	public int getSqlType() {
		return PrimitiveType.BIT;
	}
	
	/**
	 * Returns <code>this</code>
	 */	
	@Override
	public BooleanHolder asBooleanHolder() {
		return this;
	}
	
	@Override
	public BooleanHolder self() {
		return this;
	}

	public static BooleanHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return holder.asBooleanHolder();
	}

}
