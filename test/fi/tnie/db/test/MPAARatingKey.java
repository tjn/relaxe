/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.test;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.ent.value.AbstractOtherKey;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.OtherType;
import fi.tnie.db.types.PrimitiveType;

public class MPAARatingKey<
	A extends Attribute,
	E extends HasMPAARating<A, E>	
>
	extends AbstractOtherKey<A, E, MPAARating, MPAARatingType, MPAARatingHolder, MPAARatingKey<A, E>>
{	
	/**
	 *
	 */
	private static final long serialVersionUID = 3465654564903987460L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private MPAARatingKey() {
	}
	
	private MPAARatingKey(HasMPAARatingKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends Attribute,
		T extends HasMPAARating<X, T>
	>
	MPAARatingKey<X, T> get(HasMPAARatingKey<X, T> meta, X a) {
		MPAARatingKey<X, T> k = meta.getMPAARatingKey(a);
		
		if (k == null) {
			MPAARatingType kt = MPAARatingType.TYPE;
			OtherType<?> t = a.type().asOtherType();
			
			if (t != null && t.getSqlType() == PrimitiveType.OTHER && kt.getName().equals(t.getName())) {
				k = new MPAARatingKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}	

			
	@Override
	public MPAARatingType type() {
		return MPAARatingType.TYPE;
	}
	
	public void set(E e, MPAARatingHolder newValue) 
		throws EntityRuntimeException {
		e.setMPAARating(this, newValue);
	}
	
	public MPAARatingHolder get(E e) 
		throws EntityRuntimeException {
		return e.getMPAARating(this);
	}
	
	@Override
	public MPAARatingHolder newHolder(MPAARating newValue) {
		return MPAARatingHolder.valueOf(newValue);
	}

	@Override
	public void copy(E src, E dest) {
		dest.setMPAARating(this, src.getMPAARating(this));
	}

	@Override
	public MPAARatingKey<A, E> self() {
		return this;
	}

	@Override
	public MPAARatingHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return MPAARatingHolder.of(holder);
	}
}
