/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;


import fi.tnie.db.ent.Attribute;
import fi.tnie.db.rpc.TimestampHolder;

public interface HasTimestamp<A extends Attribute, E extends HasTimestamp<A, E>> {
	TimestampHolder getTimestamp(TimestampKey<A, E> key);

	void setTimestamp(TimestampKey<A, E> key, TimestampHolder newValue);
}
