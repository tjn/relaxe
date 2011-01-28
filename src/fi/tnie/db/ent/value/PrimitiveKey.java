/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;

public abstract class PrimitiveKey<
	A extends Attribute, V extends Serializable, P extends PrimitiveType<P>, H extends PrimitiveHolder<V, P>,
	E extends Entity<A, ?, ?, E>,
	K extends Key<A, V, P, H, E, K>>
	implements Key<A, V, P, H, E, K> {
	
	/**
	 *
	 */
	private static final long serialVersionUID = -3422141375386521175L;
	private A name;
	
	protected PrimitiveKey() {		
	}	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected PrimitiveKey(EntityMetaData<A, ?, ?, E> meta, A name) {
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

//	protected PrimitiveKey(EntityMetaData<A, ?, ?, E> meta, A name)
////		throws AttributeTypeException 
//		{
//		
//		setName(name);
//		
//		if (meta == null) {
//			throw new NullPointerException("meta");
//		}
//		
//		PrimitiveType<?> t = meta.getAttributeType(name);
//		
//		if (t != type()) {
//			throw new AttributeTypeException(name(), t, type()); 
//		}
//	}

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
		PrimitiveKey<?, ?, ?, ?, ?, ?> t = (PrimitiveKey<?, ?, ?, ?, ?, ?>) o;								
		return nameEquals(t);
	}
	
	private boolean nameEquals(PrimitiveKey<?, ?, ?, ?, ?, ?> pk) {		
		return name().equals(pk.name());
	}

	public abstract K normalize(EntityMetaData<A, ?, ?, E> meta);
	
}
