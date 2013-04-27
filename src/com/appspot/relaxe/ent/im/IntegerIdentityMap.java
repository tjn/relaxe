/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.im;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.value.HasInteger;
import com.appspot.relaxe.ent.value.IntegerKey;
import com.appspot.relaxe.rpc.IntegerHolder;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.IntegerType;
import com.appspot.relaxe.types.ReferenceType;

public class IntegerIdentityMap<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, ?, M, ?>,
	E extends Entity<A, R, T, E, H, ?, M, ?> & HasInteger<A, E>,
	H extends ReferenceHolder<A, R, T, E, H, M, ?>,
	M extends EntityMetaData<A, R, T, E, H, ?, M, ?>
	>
	extends AbstractKeyIdentityMap<A, R, T, E, H, M, Integer, IntegerType, IntegerHolder, IntegerKey<A, E>>
{		
	public IntegerIdentityMap(IntegerKey<A, E> key) {
		super(key);
	}
}
