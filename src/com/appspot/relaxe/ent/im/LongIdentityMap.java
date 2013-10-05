/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.im;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.value.HasLong;
import com.appspot.relaxe.ent.value.LongKey;
import com.appspot.relaxe.rpc.LongHolder;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.LongType;
import com.appspot.relaxe.types.ReferenceType;

public class LongIdentityMap<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, ?, M, ?>,
	E extends Entity<A, R, T, E, H, ?, M, ?> & HasLong<A, E>,
	H extends ReferenceHolder<A, R, T, E, H, M, ?>,
	M extends EntityMetaData<A, R, T, E, H, ?, M, ?>
	>
	extends AbstractKeyIdentityMap<A, R, T, E, H, M, Long, LongType, LongHolder, LongKey<A, E>>
{		
	public LongIdentityMap(LongKey<A, E> key) {
		super(key);
	}
}
