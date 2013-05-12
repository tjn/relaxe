/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import java.io.Serializable;

import com.appspot.relaxe.types.Type;

public interface Holder<
	V extends Serializable, 
	T extends Type<T>, 
	H extends Holder<V, T, H>
> {
	public abstract H self();
	public abstract V value();
	T getType();
	boolean isNull();
	
	boolean contentEquals(Holder<?, ?, ?> another);
}
