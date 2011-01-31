/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import java.util.Date;

import fi.tnie.db.types.DateType;
import fi.tnie.db.types.PrimitiveType;


public class DateHolder
	extends PrimitiveHolder<Date, DateType> {
		
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
	public int getSqlType() {
		return PrimitiveType.DATE;
	}
}
