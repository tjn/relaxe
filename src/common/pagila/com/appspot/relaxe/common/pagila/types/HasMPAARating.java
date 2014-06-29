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
package com.appspot.relaxe.common.pagila.types;

import com.appspot.relaxe.common.pagila.types.MPAARatingAttribute;
import com.appspot.relaxe.common.pagila.types.MPAARatingHolder;
import com.appspot.relaxe.ent.AttributeName;


public interface HasMPAARating<
	A extends AttributeName,
	R extends HasMPAARating.Read<A, R, W>,
	W extends HasMPAARating.Write<A, R, W>
>
{	
	R asRead();	
	W asWrite();	
	
	public interface Read<
		A extends AttributeName,
		R extends HasMPAARating.Read<A, R, W>,
		W extends HasMPAARating.Write<A, R, W>
	>
		extends HasMPAARating<A, R, W> {
		/**
		 * Returns the value by the key or <code>null</code> if the value is not currently present.
		 * 
		 * @param key
		 * @return The value corresponding the key.
		 * @throws NullPointerException If <code>key</code> is <code>null</code>.
		 */
		MPAARatingHolder getMPAARating(MPAARatingAttribute<A, R, W> key);		
	}
	
	public interface Write<
		A extends AttributeName,
		R extends HasMPAARating.Read<A, R, W>,
		W extends HasMPAARating.Write<A, R, W>
	> extends HasMPAARating<A, R, W> {
	
		/**
		 * Sets the value by the key.
		 * 
		 * @param key
		 * @param newValue May be null.
		 * @throws NullPointerException If <code>key</code> is <code>null</code>.
		 */
		void setMPAARating(MPAARatingAttribute<A, R, W> key, MPAARatingHolder newValue);
	}
}
