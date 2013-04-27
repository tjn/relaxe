/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.im;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.value.HasVarchar;
import com.appspot.relaxe.ent.value.VarcharKey;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.rpc.VarcharHolder;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.types.VarcharType;

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
