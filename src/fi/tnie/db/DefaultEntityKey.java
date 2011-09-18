/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.value.EntityKey;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public abstract class DefaultEntityKey<
	R extends Reference,
	T extends ReferenceType<?, R, T, E, ?, ?, S>,
	E extends Entity<?, R, T, E, ?, ?, S>,
	S extends EntityMetaData<?, R, T, E, ?, ?, S>,
	X extends Attribute,
	Y extends Reference,
	Z extends ReferenceType<X, Y, Z, V, H, VF, D>,
	V extends Entity<X, Y, Z, V, H, VF, D>,
	H extends ReferenceHolder<X, Y, Z, V, H, D>,
	VF extends EntityFactory<V, H, D, VF>,
	D extends EntityMetaData<X, Y, Z, V, H, VF, D>,
	K extends EntityKey<R, T, E, S, Z, X, Y, V, H, VF, D, K>	 
>
	implements EntityKey<R, T, E, S, Z, X, Y, V, H, VF, D, K> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4023544205660547860L;
	private S source;
	private D destination;		
	private R name;
	
	protected DefaultEntityKey() {
	}
		
	public DefaultEntityKey(S source, D destination, R name) {
		super();
		this.source = source;
		this.destination = destination;
		this.name = name;
		
		if (destination.getType() != name.type()) {
			throw new IllegalArgumentException("type mismatch: reference-type="  +name.type() + "; destination-type=" + destination.getType()); 
		} 
	}
	
	@Override
	public S getSource() {
		return this.source;
	}
	
	
	@Override
	public D getTarget() {
		return destination;
	}

	@Override
	public R name() {
		return this.name;
	}

	@Override
	public abstract H newHolder(V newValue);

	@Override
	public abstract void set(E e, H newValue);

	@Override
	public void set(E e, V newValue) {		
		e.setRef(self(), newHolder(newValue));
	}

	@Override
	public V value(E e) {
		H h = get(e);
		return (h == null) ? null : h.value();
	}
	
	public H get(E e) {
		H ref = e.getRef(self());
		return ref;
	};

			
	@Override
	public Z type() {	
		return destination.getType();
	}
	
	@Override
	public abstract K self();

	
	
	
	@Override
	public void copy(E src, E dest) {
		H v = src.getRef(self());
		dest.setRef(self(), v);		
	}

}
