/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;
import java.util.Set;

import fi.tnie.db.ent.value.EntityKey;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.ent.value.StringKey;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.rpc.StringHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

/**
 *	TODO:
		This does not handle well enough the case
		of overlapping foreign-keys:
		If the foreign key A "contains" another foreign key B (
		the set of columns in a foreign key A contains
		the columns of another foreign key B as a proper subset),
		we could assume the table <code>T</code> <code>A</code> references also contains
		a foreign key <code>C</code> which also references table <code>T</code>.

		Proper implementation should probably set conflicting references to <code>null</code>.

 * @author Administrator
 *
 * @param <A>
 * @param <R>
 * @param <Q>
 * @param <E>
 */


public abstract class DefaultEntity<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,	
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>, 
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content
>
	extends AbstractEntity<A, R, T, E, H, F, M, C> {

	/**
	 *
	 */
	private static final long serialVersionUID = 3498823449580706161L;

	protected DefaultEntity() {
		super();
	}
	
	@Override
	public <
		P extends PrimitiveType<P>,
		SH extends StringHolder<P, SH>,
		K extends StringKey<A, E, P, SH, K>
	>
	SH getString(K k) throws EntityRuntimeException {
		SH sh = get(k.self());
		return sh;
	}
		


	public <
		S extends Serializable, 
		P extends fi.tnie.db.types.PrimitiveType<P>, 
		PH extends fi.tnie.db.rpc.PrimitiveHolder<S, P, PH>, 
		K extends fi.tnie.db.ent.value.PrimitiveKey<A, E, S, P, PH, K>
	> 
	void set(K k, PH newValue) throws EntityRuntimeException {
		k.set(self(), newValue);		
	}

	public PrimitiveHolder<?, ?, ?> get(A attribute) throws EntityRuntimeException {
		return getMetaData().getKey(attribute).get(self());
	}

	
	public fi.tnie.db.rpc.PrimitiveHolder<?,?,?> value(A attribute) throws EntityRuntimeException {
		PrimitiveKey<A, E, ?, ?, ?, ?> key = getMetaData().getKey(attribute);
		return key.get(self());
	}

	public <
		S extends Serializable, 
		P extends fi.tnie.db.types.PrimitiveType<P>, 
		PH extends fi.tnie.db.rpc.PrimitiveHolder<S, P, PH>, 
		K extends fi.tnie.db.ent.value.PrimitiveKey<A, E, S, P, PH, K>
	> 
	PH get(K k) throws EntityRuntimeException {
		return k.get(self());
	}
	
		
	@Override
	public <
		RA extends Attribute,
		RR extends Reference,
		RT extends ReferenceType<RA, RR, RT, RE, RH, RF, RM, RC>,
		RE extends Entity<RA, RR, RT, RE, RH, RF, RM, RC>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM, RC>,
		RF extends EntityFactory<RE, RH, RM, RF, RC>,
		RM extends EntityMetaData<RA, RR, RT, RE, RH, RF, RM, RC>,
		RC extends Content,
		RK extends EntityKey<A, R, T, E, H, F, M, C, RA, RR, RT, RE, RH, RF, RM, RC, RK>
	> RH getRef(RK k) {
		return k.get(self());
	}
	
	public <
		RA extends Attribute,
		RR extends Reference,
		RT extends ReferenceType<RA, RR, RT, RE, RH, RF, RM, RC>,
		RE extends Entity<RA, RR, RT, RE, RH, RF, RM, RC>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM, RC>,
		RF extends EntityFactory<RE, RH, RM, RF, RC>,
		RM extends EntityMetaData<RA, RR, RT, RE, RH, RF, RM, RC>,
		RC extends Content,
		RK extends EntityKey<A, R, T, E, H, F, M, C, RA, RR, RT, RE, RH, RF, RM, RC, RK>
	> 
	void setRef(RK k, RH newValue) {		
		k.set(self(), newValue);
	}
	
	public Entity<?, ?, ?, ?, ?, ?, ?, ?> getRef(R r) {
		EntityKey<A, R, T, E, H, F, M, C, ?, ?, ?, ?, ?, ?, ?, ?, ?> k = getMetaData().getEntityKey(r);
		ReferenceHolder<?, ?, ?, ?, ?, ?, ?> rh = getRef(k.self());
		return (rh == null) ? null : rh.value();		
	}
	
	
	
	public E copy() {
		M meta = getMetaData();
		F ef = meta.getFactory();				
		E src = self(); 
		E dest = ef.newInstance();
		
		for (A a : meta.attributes()) {
			PrimitiveKey<A, E, ?, ?, ?, ?> pk = meta.getKey(a);
			pk.copy(src, dest);
		}
		
		for (R r : meta.relationships()) {
			EntityKey<A, R, T, E, H, F, M, C, ?, ?, ?, ?, ?, ?, ?, ?, ?> ek = meta.getEntityKey(r);
			ek.copy(src, dest);			
		}
		
		return dest;				
	}

	public <		
		VV extends Serializable,
		VT extends PrimitiveType<VT>,
		VH extends PrimitiveHolder<VV, VT, VH>,	
		K extends PrimitiveKey<A, E, VV, VT, VH, K>
	> 
	void remove(K key) {		
		set(key, null);
	}
	
	public <		
		VV extends Serializable,
		VT extends PrimitiveType<VT>,
		VH extends PrimitiveHolder<VV, VT, VH>,	
		K extends PrimitiveKey<A, E, VV, VT, VH, K>
	> 
	void reset(K key) {
		VH nh = key.newHolder(null);
		set(key, nh);
	}
	
	public <
		VV extends Serializable, 
		VT extends fi.tnie.db.types.PrimitiveType<VT>, 
		VH extends fi.tnie.db.rpc.PrimitiveHolder<VV, VT, VH>, 
		K extends PrimitiveKey<A, E, VV, VT, VH, K>
	> 
	boolean has(K key) {		
		return (this.get(key) != null);
	}
	
	@Override
	public abstract Set<A> attributes();
	
	
	public fi.tnie.db.rpc.ReferenceHolder<?,?,?,?,?,?,?> ref(R ref) {
		EntityKey<A, R, T, E, H, F, M, C, ?, ?, ?, ?, ?, ?, ?, ?, ?> k = getMetaData().getEntityKey(ref);
		ReferenceHolder<?, ?, ?, ?, ?, ?, ?> rh = getRef(k.self());
		return rh;
	}	
}
