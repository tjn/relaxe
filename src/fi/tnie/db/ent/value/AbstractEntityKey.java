/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public abstract class AbstractEntityKey<	
	A extends Attribute,
	R extends Reference,	
	T extends ReferenceType<A, R, T, E, H, F, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>,	
	RA extends Attribute,
	RR extends Reference,	
	RT extends ReferenceType<RA, RR, RT, RE, RH, RF, RM>,
	RE extends Entity<RA, RR, RT, RE, RH, RF, RM>,
	RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
	RF extends EntityFactory<RE, RH, RM, RF>,
	RM extends EntityMetaData<RA, RR, RT, RE, RH, RF, RM>,	
	K extends EntityKey<A, R, T, E, H, F, M, RA, RR, RT, RE, RH, RF, RM, K>
>
	implements EntityKey<A, R, T, E, H, F, M, RA, RR, RT, RE, RH, RF, RM, K> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8135907988547652994L;
	/**
	 *
	 */
	private R name;
	private T source;
		
//	protected AbstractEntityKey() {		
//	}	
	
	@Override
	public abstract RH get(E e);
	
	@Override
	public abstract void set(E e, RH newValue);
	
//	@Override
//	public void set(E e, H newValue) {		
//		e.setRef(self(), newValue);
//	}
		
	@Override
	public void set(E e, RE newValue) {
		e.setRef(self(), newHolder(newValue));
	}

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AbstractEntityKey() {
	}
	
	protected AbstractEntityKey(T source, R name) {
		setName(name);
		
		if (source == null) {
			throw new NullPointerException("source");
		}
		
		this.source = source;
	}

	private void setName(R name) {
		if (name == null) {
			throw new NullPointerException("name");
		}
		
		this.name = name;
	}

	@Override
	public R name() {
		return this.name;
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode() ^ this.name.hashCode();
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
		AbstractEntityKey<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> t = (AbstractEntityKey<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) o;								
		return nameEquals(t);
	}
	
	private boolean nameEquals(AbstractEntityKey<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> pk) {		
		return name().equals(pk.name());
	}
//	
//	@Override
//	public H get(E e) {				
//		H rh = e.getRef(self());
//		return rh;
//	}
//	
//	public void set(E e, H newValue) {
//		e.setRef(self(), newValue);		
//	}
	
	public RE value(E e) {
		RH h = get(e);
		return (h == null) ? null : h.value();		
	}
	
	@Override
	public abstract RT type();
	
	public void copy(E src, E dest) {
		K k = self();
		dest.setRef(k, src.getRef(k));
	};
	
	public void reset(E dest) {
		dest.setRef(self(), newHolder(null));
	}

	
	@Override
	public M getSource() {
		return source.getMetaData();
	}
	
}
