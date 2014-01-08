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

import java.util.Collections;
import java.util.Map;

import com.appspot.relaxe.types.ReferenceType;


public class EntitySnapshotDiff <
	A extends AttributeName,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, ?, ?, M>,
	E extends Entity<A, R, T, E, ?, ?, M>,
	M extends EntityMetaData<A, R, T, E, ?, ?, M>
>
	extends AbstractEntityDiff<A, R, T, E, M>
{
	private Map<A, Change> attributes = null;
	private Map<R, Change> references = null;

	public EntitySnapshotDiff(E original, E modified) throws EntityRuntimeException {
		super(original, modified);
		this.attributes = Collections.unmodifiableMap(super.attributes(original, modified));
		this.references = Collections.unmodifiableMap(super.references(original, modified));
	}

	@Override
	public Map<A, Change> attributes() {
		return this.attributes;
	}

	@Override
	public Map<R, Change> references() {
		return this.references;
	}


}
