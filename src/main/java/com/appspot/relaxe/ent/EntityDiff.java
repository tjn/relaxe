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


import java.util.Map;

import com.appspot.relaxe.types.ReferenceType;


/**
 * Represents the difference between two entities.
 *
 * @author Administrator
 *
 * @param <A>
 * @param <R>
 * @param <Q>
 * @param <E>
 */

public interface EntityDiff<
	A extends AttributeName,
	R extends Reference,	
	T extends ReferenceType<A, R, T, E, ?, ?, ?>,
	E extends Entity<A, R, T, E, ?, ?, ?>>
{
	/**
	 * "Total" difference between original and modified entity.
	 *
	 *  Return value <code>null</code> indicates that there are no
	 *  changes between original and modified.
	 *
	 *  Return value {@link Change.ADDITION} indicates that
	 *  the original is <code>null</code>.
	 *
	 *  Return value {@link Change.DELETION} indicates that
	 *  the modified is <code>null</code>

 	 *  Return value {@link Change.MODIFICATION} indicates that
	 *  either {@link attributes()} or {@link references()} is not empty.
	 *
	 * @return The change
	 */
	Change change();

	E getOriginal();
	E getModified();

	/**	 *
	 * @return
	 */
	Map<A, Change> attributes();

	/**
	 * @return
	 */
	Map<R, Change> references();
}
