/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import java.util.Date;

import com.appspot.relaxe.types.AbstractPrimitiveType;
import com.appspot.relaxe.types.PrimitiveType;
import com.appspot.relaxe.types.TimestampType;



public class TimestampHolder
	extends AbstractPrimitiveHolder<Date, TimestampType, TimestampHolder> {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = 4068465882098219811L;
	
	private Date value;	
	public static final TimestampHolder NULL_HOLDER = new TimestampHolder();
	
	public static TimestampHolder valueOf(Integer v) {
		return v == null ? NULL_HOLDER : valueOf(v.intValue());
	}
	
	public TimestampHolder(Date value) {
		this.value = new Date(value.getTime());
	}
	
	public static TimestampHolder valueOf(Date value) {
		return (value == null) ? NULL_HOLDER : new TimestampHolder(value);
	}
	
	private TimestampHolder() {		
	}
	
	@Override
	public Date value() {
		return this.value == null ? null : new Date(this.value.getTime());
	}
		
	@Override
	public TimestampType getType() {
		return TimestampType.TYPE;
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.TIMESTAMP;
	}
	
	@Override
	public TimestampHolder asTimestampHolder() {
		return this;
	}

	@Override
	public TimestampHolder self() {
		return this;
	}
	
	public static TimestampHolder of(PrimitiveHolder<?, ?, ?> holder) {
		return holder.asTimestampHolder();
	}
}
