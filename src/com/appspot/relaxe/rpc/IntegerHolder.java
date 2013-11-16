/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import com.appspot.relaxe.types.IntegerType;
import com.appspot.relaxe.types.PrimitiveType;


public class IntegerHolder
	extends AbstractPrimitiveHolder<Integer, IntegerType, IntegerHolder> {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = -7895663698067736815L;
	private Integer value;	
	public static final IntegerHolder NULL_HOLDER = new IntegerHolder();
		
//	public static final IntegerType TYPE = IntegerType.TYPE;
	
	private static IntegerHolder[] small;
		
	static {
		small = new IntegerHolder[256];
		                          
		for (int i = 0; i < small.length; i++) {
			small[i] = new IntegerHolder(i);			
		}
	}
		
	public static IntegerHolder valueOf(int v) {
		/**
		 * Possibly cache small ints later like java.lang.Integer
		 */
		return (v >= 0 && v < small.length) ? small[v] : new IntegerHolder(v);		
		// return new IntegerHolder(v);
	}
	
	public static IntegerHolder valueOf(Integer v) {
		return (v == null) ? NULL_HOLDER : valueOf(v.intValue());
	}
			
	public IntegerHolder(int value) {
		this.value = Integer.valueOf(value);
	}
	
	protected IntegerHolder() {		
	}
	
	@Override
	public Integer value() {
		return this.value;
	}

	@Override
	public IntegerType getType() {
		return IntegerType.TYPE;
	}

	@Override
	public int getSqlType() {
		return PrimitiveType.INTEGER;
	}
	
	public static class Factory
		implements HolderFactory<Integer, IntegerType, IntegerHolder> {

		@Override
		public IntegerHolder newHolder(Integer value) {
			return IntegerHolder.valueOf(value);
		}
	}
	
	/**
	 * Returns <code>this</code>
	 */	
	@Override
	public IntegerHolder asIntegerHolder() {
		return this;
	}
	
	@Override
	public IntegerHolder self() {
		return this;
	}

	public static IntegerHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return holder.asIntegerHolder();
	}

}
