/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.rpc.DateHolder;

public interface HasDate<
	A extends Attribute,
	E extends HasDate<A, E>
>	
{	
	DateHolder getDate(DateKey<A, E> key);
	void setDate(DateKey<A, E> key, DateHolder newValue);	
}
