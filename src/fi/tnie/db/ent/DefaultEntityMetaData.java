/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fi.tnie.db.ent.im.EntityIdentityMap;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public abstract class DefaultEntityMetaData<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,	
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content
>
	extends AbstractEntityMetaData<A, R, T, E, H, F, M, C>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8331822446687395204L;

//	private BaseTable baseTable;

	private Set<A> attributes;
	private Map<A, Column> attributeMap;
	private Map<Column, A> columnMap;
	private Set<Column> pkcols;

	private Set<R> relationships;
	private Map<R, ForeignKey> referenceMap;
	private Map<Column, Set<R>> columnReferenceMap;
		
	private transient Map<UnificationContext, EntityIdentityMap<A, R, T, E, H, M>> unificationContextMap;		
	private final transient Object unificationContextMapLock = new Object();

	protected DefaultEntityMetaData() {
	}
	
//	private void bind(BaseTable table) {
//		this.baseTable = table;
//		populateAttributes(table);
//		populateReferences(table);
//	}

	protected abstract Set<Column> populate(BaseTable table);
	
	

	protected void populateAttributes(Set<A> attributes, Map<A, Column> attributeMap, BaseTable table, Set<Column> pkc) {

		// EnumSet<K> attributes = EnumSet.allOf(keyType);
		// EnumMap<K, Column> attributeMap = new EnumMap<K, Column>(keyType);

		//	this.attributes = EnumSet.allOf(keyType);
		//	this.attributeMap = new EnumMap<A, Column>(atype);
		Map<Column, A> columnMap = new HashMap<Column, A>();

		//	EnumSet<A> pka = EnumSet.noneOf(atype);	

		for (A a : attributes) {
//			Column c = table.columnMap().get(a);
			Column c = map(table, a);

			if (c == null) {
				throw new NullPointerException(
						"no column for attribute: " + a + " in " +
						table.columns());
			}

			attributeMap.put(a, c);
			columnMap.put(c, a);

			if (c.isPrimaryKeyColumn()) {
				pkc.add(c);
			}
		}

		this.attributes = attributes;
		this.attributeMap = attributeMap;		
		this.columnMap = columnMap;
		this.pkcols = Collections.unmodifiableSet(pkc);
	}

	protected Column map(BaseTable table, A a) {
		return table.columnMap().get(a.identifier());
	}

	// protected abstract void populateReferences(BaseTable table);

	protected void populateReferences(Set<R> relationships, Map<R, ForeignKey> referenceMap, BaseTable table, Set<Column> pkc) {
//		this.references = EnumSet.allOf(rtype);
//		this.referenceMap = new EnumMap<R, ForeignKey>(rtype);
//
		Map<Column, Set<R>> rm = new HashMap<Column, Set<R>>();

		for (R r : relationships) {
			// ForeignKey fk = table.foreignKeys().get(r.identifier());
			ForeignKey fk = map(table, r);

			if (fk == null) {
				throw new NullPointerException("no such foreign key: " + r);
			}
			else {
				referenceMap.put(r, fk);
				populateColumnReferenceMap(fk, r, rm);
			}
			
			for (Column fkcol : fk.columns().keySet()) {
				if (fkcol.isPrimaryKeyColumn())  {
					pkc.add(fkcol);
				}
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
		this.pkcols = Collections.unmodifiableSet(pkc);
	}

	protected abstract ForeignKey map(BaseTable table, R r);

	private void populateColumnReferenceMap(ForeignKey fk, R r, Map<Column, Set<R>> dest) {
		for (Column fkcol : fk.columns().keySet()) {
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

	public Set<A> attributes() {
		return Collections.unmodifiableSet(this.attributes);
	}

	public Set<R> relationships() {
		return this.relationships;
	}

//	@Override
//	public BaseTable getBaseTable() {
//		return this.baseTable;
//	}

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
	public Set<Column> getPKDefinition() {
		return this.pkcols;
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
		P extends PrimitiveType<P>,
		PH extends PrimitiveHolder<V, P, PH>,
		K extends PrimitiveKey<A, E, V, P, PH, K>
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