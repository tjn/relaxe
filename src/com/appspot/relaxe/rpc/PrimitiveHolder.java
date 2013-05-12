/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import java.io.Serializable;

import com.appspot.relaxe.types.PrimitiveType;

public interface PrimitiveHolder<
	V extends Serializable, 
	T extends PrimitiveType<T>, 
	H extends PrimitiveHolder<V, T, H>
> 
	extends Holder<V, T, H>
	{
		
	public int getSqlType();
	
	@Override
	H self();
	
	/**
	 * If this holder is an;@link IntegerHolder}, returns itself as such. Otherwise, returns <code>null</code> 
	 */		
	IntegerHolder asIntegerHolder();
	
	DoubleHolder asDoubleHolder();
	
	/**
	 * If this holder is an;@link VarcharHolder}, returns itself as such. Otherwise, returns <code>null</code> 
	 */
	VarcharHolder asVarcharHolder();

	CharHolder asCharHolder();

	DateHolder asDateHolder();
	
	DecimalHolder asDecimalHolder();

	TimestampHolder asTimestampHolder();

	TimeHolder asTimeHolder();
	
	LongHolder asLongHolder();
	
	BooleanHolder asBooleanHolder();

	OtherHolder<?, ?, ?> asOtherHolder(String typeName);
	
	ArrayHolder<?, ?, ?, ?, ?> asArrayHolder(String typeName);
	
	LongVarBinaryHolder asLongVarBinaryHolder();	

}
