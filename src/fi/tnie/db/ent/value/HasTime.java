/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;


import fi.tnie.db.ent.Attribute;
import fi.tnie.db.rpc.TimeHolder;

public interface HasTime<A extends Attribute, E extends HasTime<A, E>> {
	TimeHolder getTime(TimeKey<A, E> key);

	void setTime(TimeKey<A, E> key, TimeHolder newValue);
}
