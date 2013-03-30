/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.im;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.ent.value.HasVarchar;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.types.VarcharType;

public class VarcharIdentityMap<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, ?, M, ?>,
	E extends Entity<A, R, T, E, H, ?, M, ?>  & HasVarchar<A, E>,
	H extends ReferenceHolder<A, R, T, E, H, M, ?>,
	M extends EntityMetaData<A, R, T, E, H, ?, M, ?>
>
	extends AbstractKeyIdentityMap<A, R, T, E, H, M, String, VarcharType, VarcharHolder, VarcharKey<A, E>>
{		
	public VarcharIdentityMap(VarcharKey<A, E> key) {
		super(key);
	}
}
