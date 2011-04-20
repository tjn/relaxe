/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.ent.im.AbstractKeyIdentityMap;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.types.VarcharType;

public class VarcharIdentityMap<
	A extends Attribute,
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>	
	>
	extends AbstractKeyIdentityMap<A, R, T, E, String, VarcharType, VarcharHolder, VarcharKey<A, R, T, E>>
{		
	public VarcharIdentityMap(VarcharKey<A, R, T, E> key) {
		super(key);
	}
}
