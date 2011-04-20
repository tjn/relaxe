/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.IntegerType;
import fi.tnie.db.types.ReferenceType;

public class IntegerAttributeWriter<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
>
	extends AbstractAttributeWriter<A, R, T, E, Integer, IntegerType, IntegerHolder, IntegerKey<A, R, T, E>>
{
	public IntegerAttributeWriter(IntegerKey<A, R, T, E> key, int index) {
		super(key, index);
	}
	
	/**
	 * Returns {@link PrimitiveHolder#asIntegerHolder()}. 
	 */	
	@Override
	protected IntegerHolder as(PrimitiveHolder<?, ?> ph) {
		return ph.asIntegerHolder();
	}
}
