/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import java.util.Date;

import com.appspot.relaxe.types.DateType;



public class DateHolder
	extends AbstractPrimitiveHolder<Date, DateType, DateHolder> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 8724793570826638205L;
	
	private Date value;	
	public static final DateHolder NULL_HOLDER = new DateHolder();
		
	public DateHolder(Date value) {
		this.value = (value == null) ? null : new Date(value.getTime());
	}
	
	public static DateHolder valueOf(Date value) {
		return (value == null) ? NULL_HOLDER : new DateHolder(value);
	}
	
	public static DateHolder currentDate() {
		return new DateHolder(new Date());
	}
	
	
	private DateHolder() {		
	}
	
	@Override
	public Date value() {
		return (this.value == null) ? null : new Date(this.value.getTime());
	}
		
	@Override
	public DateType getType() {
		return DateType.TYPE;
	}
	
	@Override
	public DateHolder asDateHolder() {
		return this;
	}

	@Override
	public DateHolder self() {
		return this;
	}
	
	public static DateHolder as(AbstractPrimitiveHolder<?, ?, ?> holder) {
		return holder.asDateHolder();
	}
}
