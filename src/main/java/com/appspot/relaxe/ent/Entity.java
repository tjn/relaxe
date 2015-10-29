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
import com.appspot.relaxe.ent.value.HasString;
import com.appspot.relaxe.ent.value.Attribute;
import com.appspot.relaxe.ent.value.StringAttribute;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;
import com.appspot.relaxe.value.StringHolder;
import com.appspot.relaxe.value.ValueHolder;


public interface Entity<
	A extends AttributeName,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, B, H, F, M>,
	E extends Entity<A, R, T, E, B, H, F, M>,
	B extends MutableEntity<A, R, T, E, B, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, B, H, M, F>,
	M extends EntityMetaData<A, R, T, E, B, H, F, M>
>
	extends
		HasString.Read<A, E, B>, Serializable
{

	<
		S extends Serializable,
		P extends ValueType<P>,
		PH extends ValueHolder<S, P, PH>,
		K extends Attribute<A, E, B, S, P, PH, K>
	>
	PH get(K k)
		throws EntityRuntimeException;

	<
		RA extends AttributeName,
		RR extends Reference,
		RT extends ReferenceType<RA, RR, RT, RE, RB, RH, RF, RM>,
		RE extends Entity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RB extends MutableEntity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,		
		RF extends EntityFactory<RE, RB, RH, RM, RF>,
		RM extends EntityMetaData<RA, RR, RT, RE, RB, RH, RF, RM>,
		K extends EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, K>
	>
	RH getRef(EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, K> k);


	public Entity<?, ?, ?, ?, ?, ?, ?, ?> getRef(R k);


	ValueHolder<?, ?, ?> value(A attribute) throws EntityRuntimeException;
	
	/***
	 * Returns the value of the corresponding column.
	 *
	 * If the <code>column</code> directly corresponds an attribute, the value of that attribute is returned.
	 * Otherwise, the foreign keys the <code>column</code> is part of are searched to find an entity reference.
	 *
	 * If there is no referenced entity, <code>null</code> is returned.
	 *
	 * If the entity reference <code>ref</code> (referenced by foreign key <code>F</code>) is found,
	 * <code>column</code> is mapped to the corresponding column <code>fkcol</code>
	 * in referenced table and result of the expression <code>ref.get(fkcol)</code> is returned.
	 *
	 * @param column
	 * @return Scalar value or <code>null</code>, if the value is not set
	 * @throws EntityRuntimeException
	 * @throws NullPointerException If <code>c</code> is <code>null</code>.
	 */

	ValueHolder<?, ?, ?> get(Column c) throws NullPointerException, EntityRuntimeException;

	/***
	 * Returns the reference holder by name.
	 *  
	 * If there is no referenced entity, <code>null</code> is returned.
	 *
	 * @param ref
	 * @return 
	 * @throws NullPointerException If <code>ref</code> is <code>null</code>.
	 */
	ReferenceHolder<?, ?, ?, ?, ?, ?> ref(R ref);
	
	
	/**
	 * Returns an immutable entity contains the values of primary key columns or <code>null</code> if the value of any column of the primary key is absent or <code>null</code>. 
	 * 
	 * The value of a primary key column <code>null</code> is absent or <code>null</code> if the following is true: 
	 * <code>
	 * get(c) == null || get(c).isNull()  
	 * </code>
	 * 
	 * @return
	 */	
	E toPrimaryKey();
	
	
	/**
	 * Returns an immutable entity contains the values of primary key columns or <code>null</code> if the value of any column of the primary key is absent or <code>null</code>. 
	 * 
	 * The value of a primary key column <code>null</code> is absent or <code>null</code> if the following is true: 
	 * <code>
	 * get(c) == null || get(c).isNull()  
	 * </code>
	 * 
	 * @return
	 */		
	E toPrimaryKey(com.appspot.relaxe.ent.Operation.Context ctx);
	
	

	/**
	 * Returns the meta-data object which describes the structure of this object.
	 * 
	 * @return
	 */
	M getMetaData();

	T type();

	H ref();


	
	@Override
	public <
		P extends ValueType<P>,
		SH extends StringHolder<P, SH>,		
		K extends StringAttribute<A, E, B, P, SH, K>
	>
	SH getString(K k);
	

	public E self();
	public E copy();

	boolean isIdentified();
	
	/**
	 * Set of the attributes currently present within this entity.
	 * @return
	 */
	Set<A> attributes();


	/**
	 * Returns true if and only if this entity has currently the value holder set for the attribute addressed by <code>key</code>.
	 *
	 * @param <VV>
	 * @param <VT>
	 * @param <VH>
	 * @param <K>
	 * @param key
	 */
	public <
		VV extends Serializable,
		VT extends ValueType<VT>,
		VH extends ValueHolder<VV, VT, VH>,
		K extends Attribute<A, E, B, VV, VT, VH, K>
	>
	boolean has(K key);

	/**
	 *
	 *
	 * @param <VV>
	 * @param <VT>
	 * @param <VH>
	 * @param <K>
	 * @param key
	 */
	public <
		VV extends Serializable,
		VT extends ValueType<VT>,
		VH extends ValueHolder<VV, VT, VH>,
		K extends Attribute<A, E, B, VV, VT, VH, K>
	>
	boolean match(K key, E another);
		
	boolean isMutable();
	
	B asMutable();	
	B toMutable();
	
	E asImmutable();
	E toImmutable();
	E toImmutable(Operation.Context ctx);
	
	/**
	 * 
	 * @param obj
	 * @return
	 */	
	@Override
	boolean equals(Object obj);
	
	
	boolean identityEquals(E other);
		
	
	boolean contentEquals(E other);

}
