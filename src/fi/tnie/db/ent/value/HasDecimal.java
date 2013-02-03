/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.rpc.DecimalHolder;

public interface HasDecimal<
	A extends Attribute, E extends HasDecimal<A, E>
> {
	DecimalHolder getDecimal(DecimalKey<A, E> key);
	void setDecimal(DecimalKey<A, E> key, DecimalHolder newValue);
}
