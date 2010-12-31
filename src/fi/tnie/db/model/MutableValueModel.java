/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public interface MutableValueModel<V>
	extends ValueModel<V> {

	void set(V newValue);	
	ImmutableValueModel<V> asImmutable();
}
