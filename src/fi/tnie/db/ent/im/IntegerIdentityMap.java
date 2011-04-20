/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.im;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.types.IntegerType;
import fi.tnie.db.types.ReferenceType;

public class IntegerIdentityMap<
	A extends Attribute,
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>	
	>
	extends AbstractKeyIdentityMap<A, R, T, E, Integer, IntegerType, IntegerHolder, IntegerKey<A, R, T, E>>
{		
	public IntegerIdentityMap(IntegerKey<A, R, T, E> key) {
		super(key);
	}
}
