/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.model.ValueModel;
import fi.tnie.db.model.ent.EntityModel;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.types.VarcharType;

public final class VarcharKey<
	A extends Attribute,	
	T extends ReferenceType<T, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?>
>
	extends AbstractPrimitiveKey<A, T, E, String, VarcharType, VarcharHolder, VarcharKey<A, T, E>>
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

	private VarcharKey(EntityMetaData<A, ?, T, E, ?, ?, ?> meta, A name) {
		super(meta, name);
		meta.addKey(this);
	}
	
	public static <
		X extends Attribute,		
		Z extends ReferenceType<Z, ?>,
		T extends Entity<X, ?, Z, T, ?, ?, ?>
	>
	VarcharKey<X, Z, T> get(EntityMetaData<X, ?, Z, T, ?, ?, ?> meta, X a) {
		VarcharKey<X, Z, T> k = meta.getVarcharKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = meta.getAttributeType(a);
			
			if (t != null && t.getSqlType() == PrimitiveType.VARCHAR) {
				k = new VarcharKey<X, Z, T>(meta, a);
			}			
		}
				
		return k;
	}
	
	@Override
	public VarcharType type() {
		return VarcharType.TYPE;
	}
	
	public void set(E e, VarcharHolder newValue) 
		throws EntityRuntimeException {
		e.setVarchar(this, newValue);
	}
	
	public VarcharHolder get(E e) 
		throws EntityRuntimeException {
		return e.getVarchar(this);
	}
	
	@Override
	public VarcharHolder newHolder(String newValue) {
		return VarcharHolder.valueOf(newValue);
	}

	@Override
	public void copy(E src, E dest) 
		throws EntityRuntimeException {
		dest.setVarchar(this, src.getVarchar(this));		
	}
	
	@Override
	public VarcharKey<A, T, E> self() {
		return this;
	}
	
	public ValueModel<VarcharHolder> getAttributeModel(EntityModel<A, ?, T, E, ?, ?, ?, ?> m) throws EntityRuntimeException {
		return m.getVarcharModel(self());
	}
	
}
