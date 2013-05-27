/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import com.appspot.relaxe.ent.value.LongVarBinary;
import com.appspot.relaxe.types.LongVarBinaryType;
import com.appspot.relaxe.types.PrimitiveType;



public class LongVarBinaryHolder
	extends AbstractPrimitiveHolder<LongVarBinary, LongVarBinaryType, LongVarBinaryHolder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6094556721803575825L;

	public static final LongVarBinaryHolder NULL_HOLDER = new LongVarBinaryHolder();
	
	private LongVarBinary value;
		
	
	public static LongVarBinaryHolder valueOf(LongVarBinary v) {
		if (v == null) {
			return NULL_HOLDER;
		}
		
		return new LongVarBinaryHolder(v);		
	}
	
	public LongVarBinaryHolder(LongVarBinary value) {
		this.value = value;
	}
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected LongVarBinaryHolder() {
	}
	
	
	@Override
	public LongVarBinary value() {
		return this.value;
	}

	@Override
	public LongVarBinaryType getType() {
		return LongVarBinaryType.TYPE;
	}

	@Override
	public int getSqlType() {
		return PrimitiveType.BLOB;
	}
	
	/**
	 * Returns <code>this</code>
	 */	
	@Override
	public LongVarBinaryHolder asLongVarBinaryHolder() {
		return this;
	}
	
	@Override
	public LongVarBinaryHolder self() {
		return this;
	}

	public static LongVarBinaryHolder as(AbstractPrimitiveHolder<?, ?, ?> holder) {
		return holder.asLongVarBinaryHolder();
	}
}
