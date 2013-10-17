/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import java.io.Serializable;

import com.appspot.relaxe.types.AbstractPrimitiveType;
import com.appspot.relaxe.types.PrimitiveType;

public abstract class AbstractPrimitiveHolder<
	V extends Serializable, 
	T extends PrimitiveType<T>, 
	H extends PrimitiveHolder<V, T, H>
>
	extends AbstractHolder<V, T, H>
	implements PrimitiveHolder<V, T, H>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2373967913129102220L;
	
	
	@Override
	public int getSqlType() {
		return getType().getSqlType();
	}
	
	@Override
	public String toString() {
		return super.toString() + "[" + getType().getSqlType() + "]: " + this.value();
	}
	
	// public abstract H self();
	
	
	public H as(AbstractPrimitiveType<?> type) {
		return getType().equals(type) ? self() : null;
	}	
	
	/**
	 * If this holder is an {@link IntegerHolder}, returns itself as such. Otherwise, returns <code>null</code> 
	 */		
	@Override
	public IntegerHolder asIntegerHolder() {
		return null;
	}
	
	@Override
	public DoubleHolder asDoubleHolder() {
		return null;
	}
	
	/**
	 * If this holder is an {@link VarcharHolder}, returns itself as such. Otherwise, returns <code>null</code> 
	 */
	@Override
	public VarcharHolder asVarcharHolder() {
		return null;
	}

	@Override
	public StringHolder<?, ?> asStringHolder() {
		return null;
	}
	
	@Override
	public CharHolder asCharHolder() {
		return null;
	}

	@Override
	public DateHolder asDateHolder() {
		return null;
	}
	
	@Override
	public DecimalHolder asDecimalHolder() {
		return null;
	}

	@Override
	public TimestampHolder asTimestampHolder() {
		return null;
	}

	@Override
	public TimeHolder asTimeHolder() {
		return null;
	}
	
	@Override
	public LongHolder asLongHolder() {
		return null;
	}
	
	@Override
	public BooleanHolder asBooleanHolder() {
		return null;
	}	


	@Override
	public OtherHolder<?, ?, ?> asOtherHolder(String typeName) {
		return null;
	}
	
	@Override
	public ArrayHolder<?, ?, ?, ?, ?> asArrayHolder(String typeName) {
		return null;
	}
	
	@Override
	public LongVarBinaryHolder asLongVarBinaryHolder() {
		return null;
	}	

	/**
	 * If this holder is an {@link IntervalHolder.DayTime}, returns itself as such. Otherwise, returns <code>null</code> 
	 */		
	@Override
	public IntervalHolder.DayTime asDayTimeIntervalHolder() {
		return null;
	}
	
	/**
	 * If this holder is an {@link IntervalHolder.YearMonth}, returns itself as such. Otherwise, returns <code>null</code> 
	 */		
	@Override
	public IntervalHolder.YearMonth asYearMonthIntervalHolder() {
		return null;
	}
}
