/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import com.appspot.relaxe.types.LongType;
import com.appspot.relaxe.types.PrimitiveType;


public class LongHolder
	extends AbstractPrimitiveHolder<Long, LongType, LongHolder> {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = -7183468721515930456L;
	/**
	 * 
	 */
	
	private Long value;	
	public static final LongHolder NULL_HOLDER = new LongHolder();
		
	public static LongHolder valueOf(long v) {
		/**
		 * Possibly cache small longs later.
		 */
		return new LongHolder(v);
	}
	
	public static LongHolder valueOf(Long v) {
		return v == null ? NULL_HOLDER : valueOf(v.intValue());
	}
	
	public LongHolder(long value) {
		this.value = Long.valueOf(value);
	}
	
	protected LongHolder() {		
	}
	
	@Override
	public Long value() {
		return this.value;
	}

	@Override
	public LongType getType() {
		return LongType.TYPE;
	}

	@Override
	public int getSqlType() {
		return PrimitiveType.BIGINT;
	}
	
	/**
	 * Returns <code>this</code>
	 */	
	@Override
	public LongHolder asLongHolder() {
		return this;
	}

	@Override
	public LongHolder self() {
		return this;
	}

	public static LongHolder of(PrimitiveHolder<?, ?, ?> holder) {
		return holder.asLongHolder();
	}
}
