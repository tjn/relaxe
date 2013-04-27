/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

public class Ref<T> {

	private T value;

	public Ref(T value) {
		super();
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}
}
