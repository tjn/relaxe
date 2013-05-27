/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import java.util.Date;

import com.appspot.relaxe.types.AbstractPrimitiveType;
import com.appspot.relaxe.types.PrimitiveType;
import com.appspot.relaxe.types.TimeType;


public class TimeHolder
	extends AbstractPrimitiveHolder<Date, TimeType, TimeHolder> {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = 7452231603391333418L;
	
	private Date value;	
	public static final TimeHolder NULL_HOLDER = new TimeHolder();
	
	public static TimeHolder valueOf(Integer v) {
		return v == null ? NULL_HOLDER : valueOf(v.intValue());
	}
	
	public TimeHolder(Date value) {
		this.value = new Date(value.getTime());
	}
	
	public static TimeHolder valueOf(Date value) {
		return (value == null) ? NULL_HOLDER : new TimeHolder(value);
	}
	
	private TimeHolder() {		
	}
	
	@Override
	public Date value() {
		return (this.value == null) ? null : new Date(this.value.getTime());
	}	
		
	@Override
	public TimeType getType() {
		return TimeType.TYPE;
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.TIME;
	}
	
	@Override
	public TimeHolder asTimeHolder() {
		return this;
	}

	@Override
	public TimeHolder self() {
		return this;
	}

	public static TimeHolder of(PrimitiveHolder<?, ?, ?> holder) {
		return holder.asTimeHolder();
	}
}
