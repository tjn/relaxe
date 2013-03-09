/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.test;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.env.pg.PGTextArrayKey;

public interface HasPGTextArrayKey<
	A extends Attribute,
	E extends HasPGTextArray<A, E>	
>	{

	PGTextArrayKey<A, E> getPGTextArrayKey(A a);	
	void register(PGTextArrayKey<A, E> key);
}
