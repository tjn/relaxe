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
import java.util.Map;
import java.util.Set;

import com.appspot.relaxe.ent.value.EntityKey;
import com.appspot.relaxe.ent.value.HasString;
import com.appspot.relaxe.ent.value.ValueAttribute;
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
	T extends ReferenceType<A, R, T, E, H, F, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>
>
	extends
	HasString<A, E>,
	Serializable
{

	<
		S extends Serializable,
		P extends ValueType<P>,
		PH extends ValueHolder<S, P, PH>,
		K extends ValueAttribute<A, E, S, P, PH, K>
	>
	PH get(K k)
		throws EntityRuntimeException;

	<
		RA extends AttributeName,
		RR extends Reference,
		RT extends ReferenceType<RA, RR, RT, RE, RH, RF, RM>,
		RE extends Entity<RA, RR, RT, RE, RH, RF, RM>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
		RF extends EntityFactory<RE, RH, RM, RF>,
		RM extends EntityMetaData<RA, RR, RT, RE, RH, RF, RM>,
		K extends EntityKey<A, R, T, E, H, F, M, RA, RR, RT, RE, RH, RF, RM, K>
	>
	RH getRef(EntityKey<A, R, T, E, H, F, M, RA, RR, RT, RE, RH, RF, RM, K> k);

	<
		S extends Serializable,
		P extends ValueType<P>,
		RH extends ValueHolder<S, P, RH>,
		K extends ValueAttribute<A, E, S, P, RH, K>
	>
	void set(K k, RH newValue);

	public Entity<?, ?, ?, ?, ?, ?, ?> getRef(R k);

	<
		RA extends AttributeName,
		RR extends Reference,
		RT extends ReferenceType<RA, RR, RT, RE, RH, RF, RM>,
		RE extends Entity<RA, RR, RT, RE, RH, RF, RM>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
		RF extends EntityFactory<RE, RH, RM, RF>,
		RM extends EntityMetaData<RA, RR, RT, RE, RH, RF, RM>,		
		K extends EntityKey<A, R, T, E, H, F, M, RA, RR, RT, RE, RH, RF, RM, K>
	>
	void setRef(EntityKey<A, R, T, E, H, F, M, RA, RR, RT, RE, RH, RF, RM, K> k, RH newValue);

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

	Map<Column, ValueHolder<?, ?, ?>> getPrimaryKey() throws EntityRuntimeException;

	/**
	 * Returns the meta-data object which describes the structure of this object.
	 * @return
	 */
	M getMetaData();

	T type();

	H ref();


	
	@Override
	public <
		P extends ValueType<P>,
		SH extends StringHolder<P, SH>,		
		K extends StringAttribute<A, E, P, SH, K>
	>
	SH getString(K k) throws EntityRuntimeException;

	
	@Override
	public <
		P extends ValueType<P>,
		SH extends StringHolder<P, SH>,
		K extends StringAttribute<A, E, P, SH, K>
	>
	void setString(K k, SH s) throws EntityRuntimeException;

	public <
		P extends ValueType<P>,
		SH extends StringHolder<P, SH>,
		K extends StringAttribute<A, E, P, SH, K>
	>
	void setString(K k, String s) throws EntityRuntimeException;

	public E self();
	public E copy();

	boolean isIdentified() throws EntityRuntimeException;


	/**
	 * Set of the attributes currently present within this entity.
	 * @return
	 */
	Set<A> attributes();

	/**
	 * Replaces the current value of the attribute addressed by key with null-valued holder
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
		K extends ValueAttribute<A, E, VV, VT, VH, K>
	>
	void remove(K key);

//	/**
//	 * Replaces the current value of the attribute addressed by <code>key</code> with null-valued holder.
//	 *
//	 *
//	 *
//	 * @param <VV>
//	 * @param <VT>
//	 * @param <VH>
//	 * @param <K>
//	 * @param key
//	 */
//	public <
//		VV extends Serializable,
//		VT extends PrimitiveType<VT>,
//		VH extends PrimitiveHolder<VV, VT>,
//		K extends PrimitiveKey<A, T, E, VV, VT, VH, K>
//	>
//	void reset(K key);

	/**
	 * * Resets the specified attributes.
	 */
	void reset(Iterable<A> attributes);

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
		K extends ValueAttribute<A, E, VV, VT, VH, K>
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
		K extends ValueAttribute<A, E, VV, VT, VH, K>
	>
	boolean match(K key, E another);

//	/**
//	 * Returns true if and only if this entity has currently the value holder set for the reference named by <code>r</code>.
//	 *
//	 * @param <VV>
//	 * @param <VT>
//	 * @param <VH>
//	 * @param <K>
//	 * @param key
//	 */
//	boolean has(R r);
}
