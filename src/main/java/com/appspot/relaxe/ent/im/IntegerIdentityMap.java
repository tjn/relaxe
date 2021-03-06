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
package com.appspot.relaxe.ent.im;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.value.HasInteger;
import com.appspot.relaxe.ent.value.IntegerAttribute;
import com.appspot.relaxe.types.IntegerType;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.IntegerHolder;
import com.appspot.relaxe.value.ReferenceHolder;

public class IntegerIdentityMap<
	A extends AttributeName,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, B, H, ?, M>,
	E extends Entity<A, R, T, E, B, H, ?, M> & HasInteger.Read<A, E, B>,
	B extends MutableEntity<A, R, T, E, B, H, ?, M> & HasInteger.Write<A, E, B>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	M extends EntityMetaData<A, R, T, E, B, H, ?, M>
	>
	extends AbstractKeyIdentityMap<A, R, T, E, B, H, M, Integer, IntegerType, IntegerHolder, IntegerAttribute<A, E, B>>
{		
	public IntegerIdentityMap(IntegerAttribute<A, E, B> key) {
		super(key);
	}
}
