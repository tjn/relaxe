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
import com.appspot.relaxe.ent.value.ValueAttribute;
import com.appspot.relaxe.ent.value.StringAttribute;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.rpc.StringHolder;
import com.appspot.relaxe.types.PrimitiveType;
import com.appspot.relaxe.types.ReferenceType;


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
	A extends AttributeName,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M>,	
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>, 
	M extends EntityMetaData<A, R, T, E, H, F, M>
>
	extends AbstractEntity<A, R, T, E, H, F, M> {

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
		K extends StringAttribute<A, E, P, SH, K>
	>
	SH getString(K k) throws EntityRuntimeException {
		SH sh = get(k.self());
		return sh;
	}
		


	@Override
	public <
		S extends Serializable, 
		P extends com.appspot.relaxe.types.PrimitiveType<P>, 
		PH extends com.appspot.relaxe.rpc.PrimitiveHolder<S, P, PH>, 
		K extends com.appspot.relaxe.ent.value.ValueAttribute<A, E, S, P, PH, K>
	> 
	void set(K k, PH newValue) throws EntityRuntimeException {
		k.set(self(), newValue);		
	}

	public PrimitiveHolder<?, ?, ?> get(A attribute) throws EntityRuntimeException {
		return getMetaData().getKey(attribute).get(self());
	}

	
	@Override
	public com.appspot.relaxe.rpc.PrimitiveHolder<?,?,?> value(A attribute) throws EntityRuntimeException {
		ValueAttribute<A, E, ?, ?, ?, ?> key = getMetaData().getKey(attribute);
		return key.get(self());
	}

	@Override
	public <
		S extends Serializable, 
		P extends com.appspot.relaxe.types.PrimitiveType<P>, 
		PH extends com.appspot.relaxe.rpc.PrimitiveHolder<S, P, PH>, 
		K extends com.appspot.relaxe.ent.value.ValueAttribute<A, E, S, P, PH, K>
	> 
	PH get(K k) throws EntityRuntimeException {
		return k.get(self());
	}
	
		
	@Override
	public <
		RA extends AttributeName,
		RR extends Reference,
		RT extends ReferenceType<RA, RR, RT, RE, RH, RF, RM>,
		RE extends Entity<RA, RR, RT, RE, RH, RF, RM>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
		RF extends EntityFactory<RE, RH, RM, RF>,
		RM extends EntityMetaData<RA, RR, RT, RE, RH, RF, RM>,
		RK extends EntityKey<A, R, T, E, H, F, M, RA, RR, RT, RE, RH, RF, RM, RK>
	> RH getRef(EntityKey<A, R, T, E, H, F, M, RA, RR, RT, RE, RH, RF, RM, RK> k) {
		return k.get(self());
	}
	
	@Override
	public <
		RA extends AttributeName,
		RR extends Reference,
		RT extends ReferenceType<RA, RR, RT, RE, RH, RF, RM>,
		RE extends Entity<RA, RR, RT, RE, RH, RF, RM>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
		RF extends EntityFactory<RE, RH, RM, RF>,
		RM extends EntityMetaData<RA, RR, RT, RE, RH, RF, RM>,
		RK extends EntityKey<A, R, T, E, H, F, M, RA, RR, RT, RE, RH, RF, RM, RK>
	> 
	void setRef(EntityKey<A, R, T, E, H, F, M, RA, RR, RT, RE, RH, RF, RM, RK> k, RH newValue) {		
		k.set(self(), newValue);
	}
	
	@Override
	public Entity<?, ?, ?, ?, ?, ?, ?> getRef(R r) {
		EntityKey<A, R, T, E, H, F, M, ?, ?, ?, ?, ?, ?, ?, ?> k = getMetaData().getEntityKey(r);
		ReferenceHolder<?, ?, ?, ?, ?, ?> rh = getRef(k.self());
		return (rh == null) ? null : rh.value();		
	}
	
	
	
	@Override
	public E copy() {
		M meta = getMetaData();
		F ef = meta.getFactory();				
		E src = self(); 
		E dest = ef.newEntity();
		
		for (A a : meta.attributes()) {
			ValueAttribute<A, E, ?, ?, ?, ?> pk = meta.getKey(a);
			pk.copy(src, dest);
		}
		
		for (R r : meta.relationships()) {
			EntityKey<A, R, T, E, H, F, M, ?, ?, ?, ?, ?, ?, ?, ?> ek = meta.getEntityKey(r);
			ek.copy(src, dest);			
		}
		
		return dest;				
	}

	@Override
	public <		
		VV extends Serializable,
		VT extends PrimitiveType<VT>,
		VH extends PrimitiveHolder<VV, VT, VH>,	
		K extends ValueAttribute<A, E, VV, VT, VH, K>
	> 
	void remove(K key) {		
		set(key, null);
	}
	
	public <		
		VV extends Serializable,
		VT extends PrimitiveType<VT>,
		VH extends PrimitiveHolder<VV, VT, VH>,	
		K extends ValueAttribute<A, E, VV, VT, VH, K>
	> 
	void reset(K key) {
		VH nh = key.newHolder(null);
		set(key, nh);
	}
	
	@Override
	public <
		VV extends Serializable, 
		VT extends com.appspot.relaxe.types.PrimitiveType<VT>, 
		VH extends com.appspot.relaxe.rpc.PrimitiveHolder<VV, VT, VH>, 
		K extends ValueAttribute<A, E, VV, VT, VH, K>
	> 
	boolean has(K key) {		
		return (this.get(key) != null);
	}
	
	@Override
	public abstract Set<A> attributes();
	
	
	@Override
	public com.appspot.relaxe.rpc.ReferenceHolder<?,?,?,?,?,?> ref(R ref) {
		EntityKey<A, R, T, E, H, F, M, ?, ?, ?, ?, ?, ?, ?, ?> k = getMetaData().getEntityKey(ref);
		ReferenceHolder<?, ?, ?, ?, ?, ?> rh = getRef(k.self());
		return rh;
	}	
}
