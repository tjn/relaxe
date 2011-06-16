/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.model.MutableValueModel;
import fi.tnie.db.model.ent.EntityModel;
import fi.tnie.db.rpc.DoubleHolder;
import fi.tnie.db.types.DoubleType;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public final class DoubleKey<	
	A extends Attribute,
	T extends ReferenceType<T, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?>
>
	extends AbstractPrimitiveKey<A, T, E, Double, DoubleType, DoubleHolder, DoubleKey<A, T, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1065150474303051699L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private DoubleKey() {
	}

	private DoubleKey(EntityMetaData<A, ?, T, E, ?, ?, ?> meta, A name) {
		super(meta, name);
		meta.addKey(this);
	}
	
	public static <
		X extends Attribute,		 
		Z extends ReferenceType<Z, ?>,
		T extends Entity<X, ?, Z, T, ?, ?, ?>
	>
	DoubleKey<X, Z, T> get(EntityMetaData<X, ?, Z, T, ?, ?, ?> meta, X a) {
		DoubleKey<X, Z, T> k = meta.getDoubleKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = meta.getAttributeType(a);
			
			if (t != null && t.getSqlType() == PrimitiveType.DOUBLE) {
				k = new DoubleKey<X, Z, T>(meta, a);
			}			
		}
				
		return k;
	}

	@Override
	public DoubleType type() {
		return DoubleType.TYPE;
	}

	public void set(E e, DoubleHolder newValue) 
		throws EntityRuntimeException {
		e.setDouble(this, newValue);
	}
	
	public DoubleHolder get(E e) 
		throws EntityRuntimeException {
		return e.getDouble(this);
	}

	@Override
	public DoubleHolder newHolder(Double newValue) {
		return DoubleHolder.valueOf(newValue);
	}
	
	@Override
	public void copy(E src, E dest) 
		throws EntityRuntimeException {
		dest.setDouble(this, src.getDouble(this));
	}
	
	@Override
	public DoubleKey<A, T, E> self() {
		return this;
	}
	
	@Override
	public MutableValueModel<DoubleHolder> getAttributeModel(EntityModel<A, ?, T, E, ?, ?, ?, ?> m) {
		// TODO:
		return null;
	}
}
