/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import com.appspot.relaxe.types.DecimalType;
import com.appspot.relaxe.types.AbstractPrimitiveType;


public class DecimalHolder
	extends AbstractPrimitiveHolder<Decimal, DecimalType, DecimalHolder> {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = -8533627613502905762L;
	/**
	 * 
	 */	
	private Decimal value;	
	public static final DecimalHolder NULL_HOLDER = new DecimalHolder();
	public static final DecimalHolder ZERO = new DecimalHolder(Decimal.valueOf(0));
	public static final DecimalHolder ONE = new DecimalHolder(Decimal.valueOf(1));
	
	public static DecimalHolder valueOf(long unscaled, int scale) {
		return new DecimalHolder(Decimal.valueOf(unscaled, scale));
	}
	
	public static DecimalHolder valueOf(Decimal v) {
		return (v == null) ? NULL_HOLDER : new DecimalHolder(v);
	}
	
	public DecimalHolder(Decimal value) {
		this.value = value;		
	}
	
	protected DecimalHolder() {		
	}
	
	@Override
	public Decimal value() {
		return this.value;
	}

	@Override
	public DecimalType getType() {
		return DecimalType.TYPE;
	}

	@Override
	public int getSqlType() {
		return AbstractPrimitiveType.DECIMAL;
	}

	@Override
	public DecimalHolder self() {
		return this;
	}
	
	@Override
	public DecimalHolder asDecimalHolder() {
		return this;
	}
	
	public static DecimalHolder of(AbstractPrimitiveHolder<?, ?, ?> holder) {
		return holder.asDecimalHolder();
	}
}
