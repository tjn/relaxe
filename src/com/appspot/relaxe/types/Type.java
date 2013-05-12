/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.types;

public interface Type<
	T extends Type<T>
> {
	boolean isReferenceType();
	boolean equals(T t);

	T self();
}