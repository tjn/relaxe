/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import java.io.Serializable;
import fi.tnie.db.types.PrimitiveType;

public abstract class PrimitiveHolder<V extends Serializable, T extends PrimitiveType<T>, H extends PrimitiveHolder<V, T, H>>
	extends Holder<V, T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2373967913129102220L;
	
	
	public int getSqlType() {
		return getType().getSqlType();
	}
	
	@Override
	public String toString() {
		return super.toString() + "[" + getType().getSqlType() + "]: " + this.value();
	}
	
	public abstract H self();
	
	
	public H as(PrimitiveType<?> type) {
		return getType().equals(type) ? self() : null;
	}	
	
	/**
	 * If this holder is an {@link IntegerHolder}, returns itself as such. Otherwise, returns <code>null</code> 
	 */		
	public IntegerHolder asIntegerHolder() {
		return null;
	}
	
	public DoubleHolder asDoubleHolder() {
		return null;
	}
	
	/**
	 * If this holder is an {@link VarcharHolder}, returns itself as such. Otherwise, returns <code>null</code> 
	 */
	public VarcharHolder asVarcharHolder() {
		return null;
	}

	public CharHolder asCharHolder() {
		return null;
	}

	public DateHolder asDateHolder() {
		return null;
	}
	
	public DecimalHolder asDecimalHolder() {
		return null;
	}

	public TimestampHolder asTimestampHolder() {
		return null;
	}

	public TimeHolder asTimeHolder() {
		return null;
	}
	
	public LongHolder asLongHolder() {
		return null;
	}
	
	public BooleanHolder asBooleanHolder() {
		return null;
	}	


	public OtherHolder<?, ?, ?> asOtherHolder(String typeName) {
		return null;
	}
	
	public ArrayHolder<?, ?, ?, ?, ?> asArrayHolder(String typeName) {
		return null;
	}
	
	public LongVarBinaryHolder asLongVarBinaryHolder() {
		return null;
	}	

	/**
	 * If this holder is an {@link IntervalHolder.DayTime}, returns itself as such. Otherwise, returns <code>null</code> 
	 */		
	public IntervalHolder.DayTime asDayTimeIntervalHolder() {
		return null;
	}
	
	/**
	 * If this holder is an {@link IntervalHolder.YearMonth}, returns itself as such. Otherwise, returns <code>null</code> 
	 */		
	public IntervalHolder.YearMonth asYearMonthIntervalHolder() {
		return null;
	}
}
