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
	private static final long serialVersionUID = -4023544205660547860L;
	private M source;
	private RM destination;		
	private R name;
	
	protected DefaultEntityKey() {
	}
		
	public DefaultEntityKey(M source, RM destination, R name) {
		super();
		this.source = source;
		this.destination = destination;
		this.name = name;
		
		if (destination.getType() != name.type()) {
			throw new IllegalArgumentException("type mismatch: reference-type="  +name.type() + "; destination-type=" + destination.getType()); 
		} 
	}
	
	@Override
	public M getSource() {
		return this.source;
	}
	
	
	@Override
	public RM getTarget() {
		return destination;
	}

	@Override
	public R name() {
		return this.name;
	}

	@Override
	public abstract RH newHolder(RE newValue);

	@Override
	public abstract void set(E e, RH newValue);

	@Override
	public void set(E e, RE newValue) {		
		e.setRef(self(), newHolder(newValue));
	}

	@Override
	public RE value(E e) {
		RH h = get(e);
		return (h == null) ? null : h.value();
	}
	
	public RH get(E e) {
		RH ref = e.getRef(self());
		return ref;
	};

			
	@Override
	public RT type() {	
		return destination.getType();
	}
	
	@Override
	public abstract K self();

	
	
	
	@Override
	public void copy(E src, E dest) {
		RH v = src.getRef(self());
		dest.setRef(self(), v);		
	}

}
