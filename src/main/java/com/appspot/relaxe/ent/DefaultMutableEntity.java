/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe.ent;

import java.io.Serializable;
import java.util.Set;

import com.appspot.relaxe.ent.value.EntityKey;
import com.appspot.relaxe.ent.value.Attribute;
import com.appspot.relaxe.ent.value.StringAttribute;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;
import com.appspot.relaxe.value.StringHolder;
import com.appspot.relaxe.value.ValueHolder;


public abstract class DefaultMutableEntity<
	A extends AttributeName,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, B, H, F, M>,
	E extends Entity<A, R, T, E, B, H, F, M>,
	B extends MutableEntity<A, R, T, E, B, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, B, H, M, F>,
	M extends EntityMetaData<A, R, T, E, B, H, F, M>
>	
	extends DefaultEntity<A, R, T, E, B, H, F, M>
	implements MutableEntity<A, R, T, E, B, H, F, M> {

	/**
	 *
	 */
	private static final long serialVersionUID = 3498823449580706161L;

	protected DefaultMutableEntity() {
		super();
	}
	
	@Override
	public <
		P extends ValueType<P>,
		SH extends StringHolder<P, SH>,
		K extends StringAttribute<A, E, B, P, SH, K>
	>
	SH getString(K k) throws EntityRuntimeException {
		SH sh = get(k.self());
		return sh;
	}
	
	@Override
	public <P extends ValueType<P>, SH extends StringHolder<P, SH>, K extends StringAttribute<A, E, B, P, SH, K>> void setString(K k, SH s) throws EntityRuntimeException {
		set(k.self(), s);		
	}
	
	@Override
	public <P extends ValueType<P>, SH extends StringHolder<P, SH>, K extends StringAttribute<A, E, B, P, SH, K>> void setString(K k, String s) throws EntityRuntimeException {
		setString(k, k.newHolder(s));
	}		


	@Override
	public <
		S extends Serializable, 
		P extends com.appspot.relaxe.types.ValueType<P>, 
		PH extends com.appspot.relaxe.value.ValueHolder<S, P, PH>, 
		K extends com.appspot.relaxe.ent.value.Attribute<A, E, B, S, P, PH, K>
	> 
	void set(K k, PH newValue) throws EntityRuntimeException {
		k.set(self().asMutable(), newValue);		
	}

	public ValueHolder<?, ?, ?> get(A attribute) throws EntityRuntimeException {
		return getMetaData().getKey(attribute).get(self());
	}
	
	@Override
	public abstract E self();
	

	
	@Override
	public com.appspot.relaxe.value.ValueHolder<?,?,?> value(A attribute) throws EntityRuntimeException {
		Attribute<A, E, ?, ?, ?, ?, ?> key = getMetaData().getKey(attribute);
		return key.get(self());
	}

	@Override
	public <
		S extends Serializable, 
		P extends com.appspot.relaxe.types.ValueType<P>, 
		PH extends com.appspot.relaxe.value.ValueHolder<S, P, PH>, 
		K extends com.appspot.relaxe.ent.value.Attribute<A, E, B, S, P, PH, K>
	> 
	PH get(K k) throws EntityRuntimeException {
		return k.get(self());
	}
	
		
	@Override
	public <
		RA extends AttributeName,
		RR extends Reference,
		RT extends ReferenceType<RA, RR, RT, RE, RB, RH, RF, RM>,
		RE extends Entity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RB extends MutableEntity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
		RF extends EntityFactory<RE, RB, RH, RM, RF>,
		RM extends EntityMetaData<RA, RR, RT, RE, RB, RH, RF, RM>,
		RK extends EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, RK>
	> RH getRef(EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, RK> k) {
		return k.get(self());
	}
	
	@Override
	public <
		RA extends AttributeName,
		RR extends Reference,
		RT extends ReferenceType<RA, RR, RT, RE, RB, RH, RF, RM>,
		RE extends Entity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RB extends MutableEntity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
		RF extends EntityFactory<RE, RB, RH, RM, RF>,
		RM extends EntityMetaData<RA, RR, RT, RE, RB, RH, RF, RM>,
		RK extends EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, RK>
	> 
	void setRef(EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, RK> k, RH newValue) {		
		k.set(self().asMutable(), newValue);
	}
	
	@Override
	public Entity<?, ?, ?, ?, ?, ?, ?, ?> getRef(R r) {
		EntityKey<A, R, T, E, B, H, F, M, ?, ?, ?, ?, ?, ?, ?, ?, ?> k = getMetaData().getEntityKey(r);
		ReferenceHolder<?, ?, ?, ?, ?, ?> rh = getRef(k.self());
		return (rh == null) ? null : rh.value();		
	}
	

	@Override
	public <		
		VV extends Serializable,
		VT extends ValueType<VT>,
		VH extends ValueHolder<VV, VT, VH>,	
		K extends Attribute<A, E, B, VV, VT, VH, K>
	> 
	void remove(K key) {
		set(key, null);
	}
	
	public <		
		VV extends Serializable,
		VT extends ValueType<VT>,
		VH extends ValueHolder<VV, VT, VH>,	
		K extends Attribute<A, E, B, VV, VT, VH, K>
	> 
	void reset(K key) {
		VH nh = key.newHolder(null);
		set(key, nh);
	}

	@Override
	public void reset(Iterable<A> as) {		
		M meta = getMetaData();		
		B self = asMutable();
		
		for (A a : as) {
			Attribute<A, E, B, ?, ?, ?, ?> pk = meta.getKey(a);
			pk.reset(self);
		}		
	}

	
	@Override
	public abstract Set<A> attributes();
	
	
	@Override
	public com.appspot.relaxe.value.ReferenceHolder<?,?,?,?,?,?> ref(R ref) {
		EntityKey<A, R, T, E, B, H, F, M, ?, ?, ?, ?, ?, ?, ?, ?, ?> k = getMetaData().getEntityKey(ref);
		ReferenceHolder<?, ?, ?, ?, ?, ?> rh = getRef(k.self());
		return rh;
	}
			
	@Override
	public final boolean isMutable() {
		return true;
	}
	@Override
	public final B toMutable() {
		return asMutable();
	}	
	
	@Override
	public abstract B asMutable();
	
	@Override
	public E toImmutable() {
		Operation oc = new Operation();
		
		try {			
			return toImmutable(oc.getContext());
		}
		finally {
			oc.finish();
		}
	}
	
	@Override
	public abstract E toImmutable(Operation.Context ctx);
	
	
}
