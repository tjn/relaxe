/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import com.appspot.relaxe.types.DoubleType;
import com.appspot.relaxe.types.AbstractPrimitiveType;
import com.appspot.relaxe.types.PrimitiveType;


public class DoubleHolder
	extends AbstractPrimitiveHolder<Double, DoubleType, DoubleHolder> {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = -8533627613502905762L;
	/**
	 * 
	 */	
	private Double value;	
	public static final DoubleHolder NULL_HOLDER = new DoubleHolder();
	public static final DoubleHolder ZERO = new DoubleHolder(0);
	public static final DoubleHolder ONE = new DoubleHolder(1);
	
	public static DoubleHolder valueOf(double v) {
		return 
			(v == 0) ? ZERO : 
			(v == 1) ? ONE :
			DoubleHolder.valueOf(Double.valueOf(v));
	}
	
	public static DoubleHolder valueOf(Double v) {
		return (v == null) ? NULL_HOLDER : new DoubleHolder(v);
	}
	
	public DoubleHolder(double value) {
		this.value = Double.valueOf(value);		
	}
	
	protected DoubleHolder() {		
	}
	
	@Override
	public Double value() {
		return this.value;
	}

	@Override
	public DoubleType getType() {
		return DoubleType.TYPE;
	}

	@Override
	public int getSqlType() {
		return PrimitiveType.INTEGER;
	}

	@Override
	public DoubleHolder self() {
		return this;
	}
	
	@Override
	public DoubleHolder asDoubleHolder() {
		return this;
	}

	public static DoubleHolder of(PrimitiveHolder<?, ?, ?> holder) {
		return holder.asDoubleHolder();
	}
}
