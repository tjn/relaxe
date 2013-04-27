/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import java.util.Date;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.DateHolder;
import com.appspot.relaxe.types.DateType;


public class DateAccessor<
	A extends Attribute,
	E extends HasDate<A, E>
	>
	extends AbstractPrimitiveAccessor<A, E, Date, DateType, DateHolder, DateKey<A, E>> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6884358447490019294L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private DateAccessor() {
	}

	public DateAccessor(E target, DateKey<A, E> k) {
		super(target, k);
	}

	@Override
	public void setHolder(DateHolder newHolder) {
		getTarget().setDate(key(), newHolder);
	}
}