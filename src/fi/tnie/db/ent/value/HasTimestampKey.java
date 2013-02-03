/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;

public interface HasTimestampKey<A extends Attribute, E extends HasTimestamp<A, E>> {

	TimestampKey<A, E> getTimestampKey(A a);

	void register(TimestampKey<A, E> key);
}
