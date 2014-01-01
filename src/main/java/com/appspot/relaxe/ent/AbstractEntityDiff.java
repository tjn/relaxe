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

import java.util.HashMap;
import java.util.Map;

import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.ReferenceType;


public abstract class AbstractEntityDiff<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, ?, ?, M, ?>,
	E extends Entity<A, R, T, E, ?, ?, M, ?>,
	M extends EntityMetaData<A, R, T, E, ?, ?, M, ?>
>
	implements EntityDiff<A, R, T, E>
{
	private E original;
	private E modified;

//	private static Logger logger = LoggerFactory.getLogger(AbstractEntityDiff.class);

	protected AbstractEntityDiff(E original, E modified) {
		super();
		this.original = original;
		this.modified = modified;
	}

	@Override
	public E getOriginal() {
		return original;
	}

	@Override
	public E getModified() {
		return modified;
	}

	@Override
	public Change change() {
		if (original == null && modified == null) {
			return null;
		}

		if (original == null) {
			return Change.ADDITION;
		}

		if (modified == null) {
			return Change.DELETION;
		}

		if (!attributes().isEmpty()) {
			return Change.MODIFICATION;
		}

		return null;
	}

	/**
	 * TODO: Make definition clearer.
	 *
	 * @param original
	 * @param modified
	 * @return
	 * @throws EntityException 
	 */

	protected Map<A, Change> attributes(E original, E modified) throws EntityRuntimeException {				
		EntityMetaData<A, R, T, E, ?, ?, ?, ?> meta = original.getMetaData();
		Map<A, Change> cm = new HashMap<A, Change>();

		for (A a : meta.attributes()) {
			PrimitiveHolder<?, ?, ?> o = original.value(a);
			PrimitiveHolder<?, ?, ?> m = modified.value(a);

			if ((o == null && m == null) || (o == m)) {
				continue;
			}

			if (o == null || o.isNull()) {
				cm.put(a, Change.ADDITION);
				continue;
			}

			if (m == null || m.isNull()) {
				cm.put(a, Change.DELETION);
				continue;
			}

			try {
				if (!o.contentEquals(m)) {
					cm.put(a, Change.MODIFICATION);
				}
			}
			catch (ClassCastException e) {
				cm.put(a, Change.MODIFICATION);
				// TODO: should't we throw an exception
//				logger().info(e.getMessage());
			}
		}

		return cm;
	}

	protected Map<R, Change> references(E original, E modified) throws EntityRuntimeException {
		M meta = original.getMetaData();
		Map<R, Change> cm = new HashMap<R, Change>();

		for (R r : meta.relationships()) {
			Entity<?,?,?,?,?,?,?,?> o = original.ref(r).value();
			Entity<?,?,?,?,?,?,?,?> m = modified.ref(r).value();

			if ((o == null && m == null) || (o == m)) {
				continue;
			}

			if (o == null) {
				cm.put(r, Change.ADDITION);
				continue;
			}

			if (m == null) {
				cm.put(r, Change.DELETION);
				continue;
			}

			if (o != m && primaryKeyDiffers(o, m)) {
				cm.put(r, Change.MODIFICATION);
				continue;
			}
		}

		return cm;
	}

	private
	<P extends Entity<?, ?, ?, ?, ?, ?, ?, ?>>
	boolean primaryKeyDiffers(P o, P m) throws EntityRuntimeException {
		Map<Column, PrimitiveHolder<?,?,?>> a = o.getPrimaryKey();
		Map<Column, PrimitiveHolder<?,?,?>> b = m.getPrimaryKey();

		if (a == null || b == null) {
			return a != b;
		}

		return (!a.equals(b));
	}

//	public static Logger logger() {
//		return AbstractEntityDiff.logger;
//	}
}
