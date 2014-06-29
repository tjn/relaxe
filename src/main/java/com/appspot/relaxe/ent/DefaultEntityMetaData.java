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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.appspot.relaxe.ent.im.EntityIdentityMap;
import com.appspot.relaxe.ent.value.Attribute;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.ReferenceHolder;
import com.appspot.relaxe.value.ValueHolder;


public abstract class DefaultEntityMetaData<
	A extends AttributeName,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, B, H, F, M>,
	E extends Entity<A, R, T, E, B, H, F, M>,
	B extends MutableEntity<A, R, T, E, B, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, B, H, M, F>,
	M extends EntityMetaData<A, R, T, E, B, H, F, M>
>
	extends AbstractEntityMetaData<A, R, T, E, B, H, F, M>
{
	private Set<A> attributes;
	private Map<A, Column> attributeMap;
	private Map<Column, A> columnMap;

	private Set<R> relationships;
	private Map<R, ForeignKey> referenceMap;
	private Map<Column, Set<R>> columnReferenceMap;
		
	private transient Map<UnificationContext, EntityIdentityMap<A, R, T, E, H, M>> unificationContextMap;		
	private final transient Object unificationContextMapLock = new Object();

	protected DefaultEntityMetaData() {
	}

	protected abstract void populate(BaseTable table);

	protected void populateAttributes(Set<A> attributes, Map<A, Column> attributeMap, BaseTable table) {

		Map<Column, A> columnMap = new HashMap<Column, A>();

		for (A a : attributes) {
			Column c = map(table, a);

			if (c == null) {
				throw new NullPointerException(
						"no column for attribute: " + a + " in " +
						table.getColumnMap().keySet());
			}

			attributeMap.put(a, c);
			columnMap.put(c, a);
		}

		this.attributes = attributes;
		this.attributeMap = attributeMap;		
		this.columnMap = columnMap;
	}

	protected Column map(BaseTable table, A a) {
		return table.getColumnMap().get(a.identifier());
	}


	protected void populateReferences(Set<R> relationships, Map<R, ForeignKey> referenceMap, BaseTable table) {
		Map<Column, Set<R>> rm = new HashMap<Column, Set<R>>();

		for (R r : relationships) {
			ForeignKey fk = map(table, r);

			if (fk == null) {
				throw new NullPointerException("no such foreign key: " + r);
			}
			else {
				referenceMap.put(r, fk);
				populateColumnReferenceMap(fk, r, rm);
			}			
		}

		// Ensure all the column-sets are unmodifiable after the call.
		// Column-sets which are size of 1 are expected to be created by
		// Collections.singleton and therefore unmodifiable.
		for (Map.Entry<Column, Set<R>> e : rm.entrySet()) {
			Set<R> cs = e.getValue();

			if (cs.size() > 1) {
				e.setValue(Collections.unmodifiableSet(cs));
			}
		}

		this.columnReferenceMap = rm;
		this.relationships = Collections.unmodifiableSet(relationships);
		this.referenceMap = referenceMap;
	}

	protected abstract ForeignKey map(BaseTable table, R r);

	private void populateColumnReferenceMap(ForeignKey fk, R r, Map<Column, Set<R>> dest) {
		for (Column fkcol : fk.getColumnMap().values()) {
			Set<R> rs = dest.get(fkcol);

			if (rs == null) {
				rs = Collections.singleton(r);
				dest.put(fkcol, rs);
			}
			else {
				if (rs.size() == 1) {
					rs = new HashSet<R>(rs);
					dest.put(fkcol, rs);
				}

				rs.add(r);
			}			
		}
	}

	@Override
	public Set<A> attributes() {
		return Collections.unmodifiableSet(this.attributes);
	}

	@Override
	public Set<R> relationships() {
		return this.relationships;
	}

	@Override
	public Column getColumn(A a) {
		return this.attributeMap.get(a);
	}

	@Override
	public A getAttribute(Column column) {
		if (column == null) {
			throw new NullPointerException("'column' must not be null");
		}

		return this.columnMap.get(column);
	}

	@Override
	public ForeignKey getForeignKey(R r) {
		return this.referenceMap.get(r);
	}

	@Override
	public Set<R> getReferences(Column c) {
		return this.columnReferenceMap.get(c);
	}

	protected <
		V extends Serializable, 
		P extends ValueType<P>,
		PH extends ValueHolder<V, P, PH>,
		K extends Attribute<A, E, B, V, P, PH, K>
	>
	K key(A name, Map<A, K> src) {
		if (name == null) {
			throw new NullPointerException("name");
		}
				
		return (src == null) ? null : src.get(name);
	}
	
	public EntityIdentityMap<A, R, T, E, H, M> createIdentityMap() {
		return new DefaultIdentityMap();
	}

	@Override
	public EntityIdentityMap<A, R, T, E, H, M> getIdentityMap(final UnificationContext ctx) {
		if (ctx == null) {
			throw new NullPointerException("ctx");
		}
		
		synchronized (unificationContextMapLock) {
			final Map<UnificationContext, EntityIdentityMap<A, R, T, E, H, M>> icm = getUnificationContextMap();
			EntityIdentityMap<A, R, T, E, H, M> im = icm.get(ctx);
			
			if (im == null) {
				im = createIdentityMap();				
				icm.put(ctx, im);
				
				ctx.add(new ContextRegistration() {		
					@Override
					public void remove() {
						synchronized (unificationContextMapLock) {
							icm.remove(ctx);
						}
					}
				});
			}
			
			return im;
		}
	}
	
	private Map<UnificationContext, EntityIdentityMap<A, R, T, E, H, M>> getUnificationContextMap() {
		synchronized (unificationContextMapLock) {
			if (unificationContextMap == null) {
				unificationContextMap = new HashMap<UnificationContext, EntityIdentityMap<A, R, T, E, H, M>>();			
			}
	
			return unificationContextMap;
		}
	}
	
	private class DefaultIdentityMap
		implements EntityIdentityMap<A, R, T, E, H, M> {

		@Override
		public H get(E v) {
			return v.ref();
		}		
	}
	
//	public H unify(UnificationContext ctx, E e) throws EntityRuntimeException {
//		EntityIdentityMap<A, R, T, E, H, M> im = getIdentityMap(ctx);
//		return im.get(e);
//	}
}