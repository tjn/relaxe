/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import java.util.Date;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.TimestampHolder;
import com.appspot.relaxe.types.TimestampType;


public class TimestampAccessor<
	A extends Attribute,
	E extends HasTimestamp<A, E>
>
	extends AbstractPrimitiveAccessor<A, E, Date, TimestampType, TimestampHolder, TimestampKey<A, E>> {

	/**
	 *
	 */
	private static final long serialVersionUID = -7871095796458117502L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private TimestampAccessor() {
	}

	public TimestampAccessor(E target, TimestampKey<A, E> k) {
		super(target, k);
	}
}