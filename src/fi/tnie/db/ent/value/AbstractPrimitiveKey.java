/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public abstract class AbstractPrimitiveKey<
	A extends Attribute,	
	T extends ReferenceType<A, ?, T, E, ?, ?, ?, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?, ?>,
	V extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<V, P>,
	K extends PrimitiveKey<A, T, E, V, P, H, K>
>
	implements PrimitiveKey<A, T, E, V, P, H, K> {
	
	/**
	 *
	 */
	private static final long serialVersionUID = -3422141375386521175L;
	private A name;
	
	protected AbstractPrimitiveKey() {		
	}	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected AbstractPrimitiveKey(EntityMetaData<A, ?, ?, E, ?, ?, ?, ?> meta, A name) {
		setName(name);
		
		if (meta == null) {
			throw new NullPointerException("meta");
		}		
	}

	private void setName(A name) {
		if (name == null) {
			throw new NullPointerException("name");
		}
		
		this.name = name;
	}

	@Override
	public A name() {
		return this.name;
	}	

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			throw new NullPointerException("o");
		}
		
		if (o == this) {
			return true;
		}
		
		if(!getClass().equals(o.getClass())) {
			return false;
		}
		
		// Since getClass().equals(o.getClass()) implies t.type().getSqlType() == type().getSqlType()
		// we only need to check the name:		
		AbstractPrimitiveKey<?, ?, ?, ?, ?, ?, ?> t = (AbstractPrimitiveKey<?, ?, ?, ?, ?, ?, ?>) o;				
		return nameEquals(t);
	}
	
	private boolean nameEquals(AbstractPrimitiveKey<?, ?, ?, ?, ?, ?, ?> pk) {		
		return name().equals(pk.name());
	}
	
	public void reset(E dest)	
		throws EntityRuntimeException {
		dest.set(self(), newHolder(null));
	}
}
