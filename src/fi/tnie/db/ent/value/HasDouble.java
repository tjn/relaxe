/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.rpc.DoubleHolder;

public interface HasDouble<A extends Attribute, E extends HasDouble<A, E>> {
	DoubleHolder getDouble(DoubleKey<A, E> key);
	void setDouble(DoubleKey<A, E> key, DoubleHolder newValue);
}
