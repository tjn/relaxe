/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.types.VarcharType;

public final class VarcharKey<
	A extends Attribute, 
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
>
	extends AbstractPrimitiveKey<A, R, T, E, String, VarcharType, VarcharHolder, VarcharKey<A, R, T, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 128524051109455630L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private VarcharKey() {
	}

	private VarcharKey(EntityMetaData<A, R, T, E> meta, A name) {
		super(meta, name);
		meta.addKey(this);
	}
	
	public static <
		X extends Attribute,
		Y,
		Z extends ReferenceType<Z>,
		T extends Entity<X, Y, Z, T>
	>
	VarcharKey<X, Y, Z, T> get(EntityMetaData<X, Y, Z, T> meta, X a) {
		VarcharKey<X, Y, Z, T> k = meta.getVarcharKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = meta.getAttributeType(a);
			
			if (t != null && t.getSqlType() == PrimitiveType.VARCHAR) {
				k = new VarcharKey<X, Y, Z, T>(meta, a);
			}			
		}
				
		return k;
	}
	
	@Override
	public VarcharType type() {
		return VarcharType.TYPE;
	}
	
	public void set(E e, VarcharHolder newValue) {
		e.setVarchar(this, newValue);
	}
	
	public VarcharHolder get(E e) {
		return e.getVarchar(this);
	}
	
	@Override
	public VarcharHolder newHolder(String newValue) {
		return VarcharHolder.valueOf(newValue);
	}
	
//	@Override
//	public VarcharKey<A, E> normalize(EntityMetaData<A, ?, ?, E> meta) {
//		return meta.getVarcharKey(name());
//	}
}
