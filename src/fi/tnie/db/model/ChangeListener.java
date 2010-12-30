/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public interface ChangeListener<V> {
	void changed(V from, V to);
}
