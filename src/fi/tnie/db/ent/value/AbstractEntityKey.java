/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public abstract class AbstractEntityKey<	
	R extends Reference,
	T extends ReferenceType<T, S>,
	E extends Entity<?, R, T, E, ?, ?, S>,
	S extends EntityMetaData<?, R, T, E, ?, ?, S>,
	P extends ReferenceType<P, D>,	
	V extends Entity<?, ?, P, V, H, ?, D>,
	H extends ReferenceHolder<?, ?, P, V, H, D>,
	D extends EntityMetaData<?, ?, P, V, H, ?, D>,
	K extends EntityKey<R, T, E, S, P, V, H, D, K>
>
	implements EntityKey<R, T, E, S, P, V, H, D, K> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8135907988547652994L;
	/**
	 *
	 */
	private R name;
	private S meta;
	
//	protected AbstractEntityKey() {		
//	}	
	
	@Override
	public abstract H get(E e);
	
	@Override
	public abstract void set(E e, H newValue);
	
//	@Override
//	public void set(E e, H newValue) {		
//		e.setRef(self(), newValue);
//	}
		
	@Override
	public void set(E e, V newValue) {
		e.setRef(self(), newHolder(newValue));
	}
		
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected AbstractEntityKey(S meta, R name) {
		setName(name);
		
		if (meta == null) {
			throw new NullPointerException("meta");
		}		
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
		AbstractEntityKey<?, ?, ?, ?, ?, ?, ?, ?, ?> t = (AbstractEntityKey<?, ?, ?, ?, ?, ?, ?, ?, ?>) o;								
		return nameEquals(t);
	}
	
	private boolean nameEquals(AbstractEntityKey<?, ?, ?, ?, ?, ?, ?, ?, ?> pk) {		
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
	
	public V value(E e) {
		H h = get(e);
		return (h == null) ? null : h.value();		
	}
	
	@Override
	public abstract P type();
	
	public void copy(E src, E dest) {
		K k = self();
		dest.setRef(k, src.getRef(k));
	};
	
	public void clear(E src) {
		src.setRef(self(), newHolder(null));
	}

	
	@Override
	public S getSource() {	
		return this.meta;
	}
}
