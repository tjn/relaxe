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
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.types.LongType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.LongHolder;
import com.appspot.relaxe.value.ValueHolder;

public final class LongAttribute<
	A extends AttributeName,
	E extends HasLong.Read<A, E, B>,
	B extends HasLong.Write<A, E, B>
>
extends AbstractAttribute<A, E, B, Long, LongType, LongHolder, LongAttribute<A, E, B>>	
{	
/**
 *
 */
private static final long serialVersionUID = 3465654564903987460L;

/**
 * No-argument constructor for GWT Serialization
 */	
private LongAttribute() {
}

private LongAttribute(A name) {
	super(name);		
}

public static <
	X extends AttributeName,
	T extends HasLong.Read<X, T, S>,
	S extends HasLong.Write<X, T, S>
>
LongAttribute<X, T, S> get(HasLongAttribute<X, T, S> meta, X a) {
	LongAttribute<X, T, S> k = meta.getLongAttribute(a);
	
	if (k == null) {
		ValueType<?> t = a.type();
		
		if (t != null && LongType.TYPE.equals(t)) {
			k = new LongAttribute<X, T, S>(a);
			meta.register(k);
		}
	}
			
	return k;
}
	
@Override
public LongType type() {
	return LongType.TYPE;
}

@Override
public void set(B e, LongHolder newValue) {
	e.setLong(this, newValue);
}

@Override
public LongHolder get(E e) {
	return e.getLong(self());
}

@Override
public LongHolder newHolder(Long newValue) {
	return LongHolder.valueOf(newValue);
}

@Override
public void copy(E src, B dest) {
	dest.setLong(this, src.getLong(this));
}

@Override
public LongAttribute<A, E, B> self() {
	return this;
}

@Override
public void reset(B dest) throws EntityRuntimeException {
	dest.setLong(this, LongHolder.NULL_HOLDER);
}
	
@Override
public LongHolder as(ValueHolder<?, ?, ?> unknown) {
	return unknown.asLongHolder();
}
}
