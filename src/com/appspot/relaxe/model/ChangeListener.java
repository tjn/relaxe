/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model;

public interface ChangeListener<V> {
	void changed(V from, V to);
}
