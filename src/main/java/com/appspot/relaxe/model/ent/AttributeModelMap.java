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
package com.appspot.relaxe.model.ent;

import java.io.Serializable;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.ent.value.Attribute;
import com.appspot.relaxe.model.ValueModel;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.ValueHolder;


public interface AttributeModelMap<
	A extends AttributeName,
	V extends Serializable,
	P extends ValueType<P>,
	H extends ValueHolder<V, P, H>,
	T extends ReferenceType<A, ?, T, E, B, ?, ?, ?>,
	E extends Entity<A, ?, T, E, B, ?, ?, ?>,
	B extends MutableEntity<A, ?, T, E, B, ?, ?, ?>,
	D extends AttributeModelMap<A, V, P, H, T, E, B, D>
>	
{	
	D self();
	
	<			
		K extends Attribute<A, E, B, V, P, H, K>
	>	
	ValueModel<H> attr(K k) throws EntityRuntimeException;		
}
