/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import java.util.Date;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.TimeHolder;
import com.appspot.relaxe.types.TimeType;


public class TimeAccessor<
	A extends Attribute,	
	E extends HasTime<A, E>
>
	extends AbstractPrimitiveAccessor<A, E, Date, TimeType, TimeHolder, TimeKey<A, E>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7412092659898638958L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private TimeAccessor() {
	}

	public TimeAccessor(E target, TimeKey<A, E> k) {
		super(target, k);
	}
}