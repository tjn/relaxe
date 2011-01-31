/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.Type;
import fi.tnie.db.types.VarcharType;

public final class VarcharKey<A extends Attribute, E extends Entity<A, ?, ?, E>>
	extends PrimitiveKey<A, String, VarcharType, VarcharHolder, E, VarcharKey<A, E>>
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

	private VarcharKey(EntityMetaData<A, ?, ?, E> meta, A name) {
		super(meta, name);
		meta.addKey(this);
	}
	
	public static <
		X extends Attribute,
		T extends Entity<X, ?, ?, T>
	>
	VarcharKey<X, T> get(EntityMetaData<X, ?, ?, T> meta, X a) {
		VarcharKey<X, T> k = meta.getVarcharKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = meta.getAttributeType(a);
			
			if (t != null && t.getSqlType() == Type.VARCHAR) {
				k = new VarcharKey<X, T>(meta, a);
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
	
	@Override
	public VarcharKey<A, E> normalize(EntityMetaData<A, ?, ?, E> meta) {
		return meta.getVarcharKey(name());
	}
}
