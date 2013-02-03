/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;

public interface HasDateKey<
	A extends Attribute,
	E extends HasDate<A, E>	
>	{

	DateKey<A, E> getDateKey(A a);	
	void register(DateKey<A, E> key);
}
