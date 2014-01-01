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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.appspot.relaxe.ent.query.EntityQueryAttributeValueReference;
import com.appspot.relaxe.ent.query.EntityQueryColumnReference;
import com.appspot.relaxe.ent.query.EntityQueryExpressionSortKey;
import com.appspot.relaxe.ent.query.EntityQueryPredicate;
import com.appspot.relaxe.ent.query.EntityQuerySortKey;
import com.appspot.relaxe.ent.query.EntityQueryValueExpression;
import com.appspot.relaxe.ent.query.EntityQueryValueReference;
import com.appspot.relaxe.ent.query.DefaultEntityQueryPredicate;
import com.appspot.relaxe.ent.value.PrimitiveKey;
import com.appspot.relaxe.ent.value.StringKey;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.expr.ImmutableValueParameter;
import com.appspot.relaxe.expr.op.Comparison;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.PrimitiveType;
import com.appspot.relaxe.types.ReferenceType;

public class DefaultEntityQuery<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content,	
	RE extends EntityQueryElement<A, R, T, E, H, F, M, C, RE>
> 
	implements EntityQuery<A, R, T, E, H, F, M, C, RE>	
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3347958818803591937L;
	private RE root;
	private Collection<EntityQueryPredicate> predicates;
	private List<EntityQuerySortKey> sortKeys;
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected DefaultEntityQuery() {
	}
	
	public DefaultEntityQuery(RE root, EntityQueryPredicate predicate) {
		this.root = root;
		
		if (predicate != null) {
			this.predicates = new ArrayList<EntityQueryPredicate>();
			this.predicates.add(predicate);
		}
	}
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected DefaultEntityQuery(RE root, Collection<EntityQueryPredicate> predicates, List<EntityQuerySortKey> sortKeys) {
		this.root = root;
		this.predicates = predicates;
		this.sortKeys = sortKeys;
	}

	@Override
	public RE getRootElement() {
		return root;
	}

	@Override
	public Collection<EntityQueryPredicate> predicates() {
		if (predicates == null) {
			return Collections.emptyList();
		}
				
		return Collections.unmodifiableCollection(predicates);
	}

	@Override
	public List<EntityQuerySortKey> sortKeys() {
		if (sortKeys == null) {
			return Collections.emptyList();
		}
				
		return Collections.unmodifiableList(sortKeys);
	}

	
	public static class Builder<
		A extends Attribute,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, H, F, M, C>,
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends Content,	
		RE extends EntityQueryElement<A, R, T, E, H, F, M, C, RE>
	>
		implements EntityQuery.Builder<A, R, T, E, H, F, M, C, RE> {
		
		private Collection<EntityQueryPredicate> predicates;
		private List<EntityQuerySortKey> sortKeys;		
		private RE root;
		
		public Builder(RE root) {
			super();
			setRootElement(root);
		}
		
		@Override
		public void setRootElement(RE root) {
			this.root = root;
		}
		
		protected Collection<EntityQueryPredicate> getPredicates() {
			return (predicates == null || predicates.isEmpty()) ? null : new ArrayList<EntityQueryPredicate>(this.predicates);
		}
		
		protected List<EntityQuerySortKey> getSortKeys() {
			return (sortKeys == null || sortKeys.isEmpty()) ? null : new ArrayList<EntityQuerySortKey>(this.sortKeys);
		}
				
		public void addPredicate(A attribute, Comparison.Op op, ValueExpression v) {
			EntityQueryValueReference a = new EntityQueryAttributeValueReference<A, RE>(this.root, attribute);
			EntityQueryValueExpression b = new EntityQueryValueExpression(v);			
			addPredicate(new DefaultEntityQueryPredicate(op, a, b));			
		}
		
		@Override
		public void addPredicate(Identifier column, Comparison.Op op, ValueExpression v) {
			EntityQueryValueReference a = new EntityQueryColumnReference(this.root, column);
			EntityQueryValueExpression b = new EntityQueryValueExpression(v);
			addPredicate(new DefaultEntityQueryPredicate(op, a, b));
		}
		
		public void addPredicate(A attribute, ValueExpression v) {			
			addPredicate(attribute, Comparison.Op.EQ, v);
		}
		
		@Override
		public
		<
			V extends Serializable,
			P extends PrimitiveType<P>,
			W extends PrimitiveHolder<V, P, W>,
			K extends PrimitiveKey<A, E, V, P, W, K>
		>
		void addPredicate(K key, Comparison.Op op, W value) {
			EntityQueryValueReference a = new EntityQueryAttributeValueReference<A, RE>(this.root, key.name());
			
			M meta = this.root.getMetaData();
			Column column = meta.getColumn(key.name());
			ImmutableValueParameter<V, P, W> vp = new ImmutableValueParameter<V, P, W>(column, value);
			EntityQueryValueExpression b = new EntityQueryValueExpression(vp);
			addPredicate(new DefaultEntityQueryPredicate(op, a, b));			
		}
		
		public
		<
			XA extends com.appspot.relaxe.ent.Attribute,
			XE extends com.appspot.relaxe.ent.Entity<XA, ?, ?, XE, ?, ?, XM, ?>,
			XM extends com.appspot.relaxe.ent.EntityMetaData<XA, ?, ?, XE, ?, ?, XM, ?>,
			XQ extends EntityQueryElement<XA, ?, ?, XE, ?, ?, XM, ?, XQ>,
			V extends Serializable,
			P extends PrimitiveType<P>,
			W extends PrimitiveHolder<V, P, W>,
			K extends PrimitiveKey<XA, XE, V, P, W, K>
		>
		EntityQueryPredicate newPredicate(XQ element, K key, Comparison.Op op, W value) {
			XM meta = element.getMetaData();			
			Column column = meta.getColumn(key.name());									
			EntityQueryValueReference a = new EntityQueryAttributeValueReference<XA, XQ>(element, key.name());			
			ImmutableValueParameter<V, P, W> vp = new ImmutableValueParameter<V, P, W>(column, value);
			EntityQueryValueExpression b = new EntityQueryValueExpression(vp);
			return new DefaultEntityQueryPredicate(op, a, b);			
		}
		
		public
		<
			XA extends com.appspot.relaxe.ent.Attribute,
			XE extends com.appspot.relaxe.ent.Entity<XA, ?, ?, XE, ?, ?, XM, ?>,
			XM extends com.appspot.relaxe.ent.EntityMetaData<XA, ?, ?, XE, ?, ?, XM, ?>,
			XQ extends EntityQueryElement<XA, ?, ?, XE, ?, ?, XM, ?, XQ>,
			V extends Serializable,
			P extends PrimitiveType<P>,
			W extends PrimitiveHolder<V, P, W>,
			K extends PrimitiveKey<XA, XE, V, P, W, K>
		>
		EntityQueryPredicate newPredicate(XQ element, K key, XE ent) {
			return newPredicate(element, key, Comparison.Op.EQ, ent.get(key));
		}
		
		@Override
		public
		<
			V extends Serializable,
			P extends PrimitiveType<P>,
			W extends PrimitiveHolder<V, P, W>,
			K extends PrimitiveKey<A, E, V, P, W, K>
		>
		void addPredicate(K key, W holder) {
			addPredicate(key, Comparison.Op.EQ, holder);			
		}
		
		@Override
		public <
			P extends PrimitiveType<P>,
			W extends PrimitiveHolder<String, P, W>,
			K extends StringKey<A, E, P, W, K>
		>
		void addPredicate(K key, String value) {
			addPredicate(key, Comparison.Op.EQ, value);
		}
		
		@Override
		public <
			P extends PrimitiveType<P>,
			W extends PrimitiveHolder<String, P, W>,
			K extends StringKey<A, E, P, W, K>
		>
		void addPredicate(K key, Comparison.Op op, String value) {			
			addPredicate(key, op, key.newHolder(value));			
		}

		@Override
		public void addPredicate(EntityQueryPredicate predicate) {
			if (predicate == null) {
				throw new NullPointerException("predicate");
			}
						
			if (this.predicates == null) {
				this.predicates = new ArrayList<EntityQueryPredicate>();				
			}
			
			this.predicates.add(predicate);
		}
		
		
		@Override
		public 
		<
			XA extends com.appspot.relaxe.ent.Attribute,
			XE extends Entity<XA, ?, ?, XE, ?, ?, ?, ?>,
			XQ extends EntityQueryElement<XA, ?, ?, XE, ?, ?, ?, ?, XQ>
		>		
		void asc(EntityQueryElement<XA, ?, ?, XE, ?, ?, ?, ?, XQ> element, PrimitiveKey<XA, XE, ?, ?, ?, ?> attribute) {
			asc(element, attribute.name());
		}
		
		@Override
		public 
		<
			XA extends com.appspot.relaxe.ent.Attribute,
			XE extends Entity<XA, ?, ?, XE, ?, ?, ?, ?>,
			XQ extends EntityQueryElement<XA, ?, ?, XE, ?, ?, ?, ?, XQ>
		>		
		void desc(EntityQueryElement<XA, ?, ?, XE, ?, ?, ?, ?, XQ> element, PrimitiveKey<XA, XE, ?, ?, ?, ?> key) {
			desc(element, key.name());
		}
		
		
		@Override
		public void asc(PrimitiveKey<A, E, ?, ?, ?, ?> key) {
			asc(this.root, key);
		}
		
		@Override
		public void desc(PrimitiveKey<A, E, ?, ?, ?, ?> key) {
			desc(this.root, key);
		}
		
		@Override
		public 
		<
			XA extends com.appspot.relaxe.ent.Attribute,			
			XQ extends EntityQueryElement<XA, ?, ?, ?, ?, ?, ?, ?, XQ>
		>		
		void asc(EntityQueryElement<XA, ?, ?, ?, ?, ?, ?, ?, XQ> element, XA attribute) {
			addSortKey(new EntityQueryExpressionSortKey.Asc(element, attribute));			
		}
		
		@Override
		public 
		<
			XA extends com.appspot.relaxe.ent.Attribute,			
			XQ extends EntityQueryElement<XA, ?, ?, ?, ?, ?, ?, ?, XQ>
		>		
		void desc(EntityQueryElement<XA, ?, ?, ?, ?, ?, ?, ?, XQ> element, XA attribute) {
			addSortKey(new EntityQueryExpressionSortKey.Desc(element, attribute));			
		}
		
		
		@Override
		public void addSortKey(EntityQuerySortKey key) {
			if (key == null) {
				throw new NullPointerException("key");
			}
			
			if (this.sortKeys == null) {
				this.sortKeys = new ArrayList<EntityQuerySortKey>();				
			}
			
			this.sortKeys.add(key);
		}
		
		
					
		@Override
		public RE getRootElement() {
			return this.root;
		}

		@Override
		public EntityQuery<A, R, T, E, H, F, M, C, RE> newQuery() {						
			return new DefaultEntityQuery<A, R, T, E, H, F, M, C, RE>(
					getRootElement(), this.predicates, this.sortKeys);
		}		
		
	}
	
	
	
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -5505364328412305185L;
//			
//	private transient DefaultTableExpression query;
//	private transient QueryExpression queryExpression;
//	private transient TableReference rootRef;
//		
//	private transient List<Predicate> predicateList;
//
//	private transient Map<TableReference, EntityMetaData<?, ?, ?, ?, ?, ?, ?, ?>> metaDataMap;
//	
//	private transient LinkedHashMap<Integer, TableReference> originMap;	
//	private transient Map<JoinKey, TableReference> referenceMap;
//	
//	private transient Map<EntityQueryTemplateAttribute, ColumnReference> columnMap;
//	
//	private transient Map<EntityQuerySortKey<?>, ColumnReference> sortKeyColumnMap = new HashMap<EntityQuerySortKey<?>, ColumnReference>();	
//	private transient Map<EntityQueryPredicate<?>, ColumnReference> predicateColumnMap = new HashMap<EntityQueryPredicate<?>, ColumnReference>();
//	
//	private transient Map<EntityQueryPredicate<?>, TableReference> tableReferenceMap = new HashMap<EntityQueryPredicate<?>, TableReference>();
//	
//	private transient List<ColumnReference> rootPrimaryKey; // NS
//		
//	public static final class Entry {				
//		private Map<ForeignKey, TableReference> referenceMap;
//		
//		public Entry() {
//			super();			
//		}
//		
//		public Map<ForeignKey, TableReference> getReferenceMap() {
//			if (referenceMap == null) {
//				referenceMap = new HashMap<ForeignKey, TableReference>();				
//			}
//
//			return referenceMap;
//		}
//	}
//	
//	
//	private RE root;
//						
//	/**
//	 * No-argument constructor for GWT Serialization
//	 */
//	protected DefaultEntityQuery() {
//	}
//	
//	public DefaultEntityQuery(RE root) {		
//		if (root == null) {
//			throw new NullPointerException();
//		}
//		
//		this.root = root;
//	}
//
//
//
//	private void init() throws EntityRuntimeException {
//		if (isInitialized()) {
//			return;
//		}
//		
////		logger().debug("init - enter");
//		
////		RT root = this.template;				
//		
//		BaseTable table = getRootElement().getMetaData().getBaseTable();
//
//		if (table == null) {
//			throw new NullPointerException("DefaultEntityTemplateQuery: getMetaData().getBaseTable()");
//		}		
//				
//		List<EntityQueryPredicate<?>> apl = new ArrayList<EntityQueryPredicate<?>>();
//		
//		{
//			Set<EntityQueryElement<?,?,?,?,?,?,?,?,?>> visited = 
//				new HashSet<EntityQueryElement<?,?,?,?,?,?,?,?,?>>();		
//			addTemplatePredicates(root, apl, visited);
//		}
//			
//		DefaultTableExpression te = new DefaultTableExpression();
//		
//		Map<EntityQueryElementTemplate<?,?,?,?,?,?,?,?,?,?>, TableReference> visited = 
//			new HashMap<EntityQueryElementTemplate<?,?,?,?,?,?,?,?,?,?>, TableReference>();
//		
//		Map<EntityQueryElementTemplate<?,?,?,?,?,?,?,?,?,?>, TableReference> trm = 
//			new HashMap<EntityQueryElementTemplate<?,?,?,?,?,?,?,?,?,?>, TableReference>();
//		
//		Map<EntityQueryElementTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, Entry> rem = 
//			new HashMap<EntityQueryElementTemplate<?,?,?,?,?,?,?,?,?,?>, Entry>();
//			
//		this.rootRef = populateTableReferenceMap(root, trm, rem);	
//		
////		logger().debug("trm.size(): " + trm.size());
////		logger().debug("rem.size(): " + rem.size());
//		
//		AbstractTableReference tref = fromTemplate(null, root, null, null, null, te, visited, trm, rem);
//		
////		logger().debug("ref: " + tref);		
////		logger().debug("originMap: " + originMap);
////		logger().debug("metaDataMap: " + metaDataMap);
//		
//		te.setFrom(new From(tref));
//								
//		List<EntityQueryPredicate<?>> pl = apl;		
//		
//		if (pl != null && (!pl.isEmpty())) {
//			Predicate ap = null;
//			
//			for (EntityQueryPredicate<?> p : pl) {
//				TableReference ptr = tableReferenceMap.get(p);				
//				ColumnReference cr = predicateColumnMap.get(p);				
//				Predicate qp = p.predicate(ptr, cr);
//				ap = AndPredicate.newAnd(ap, qp);
//			}
//			
//			te.setWhere(new Where(ap));
//		}	
//		
//		
//				
//		this.query = te;
//		
//		// List<EntityQuerySortKey<?>> sortKeyList = root.allSortKeys();
//		// TODO: FIX REFACTORED
//		List<EntityQuerySortKey<?>> sortKeyList = Collections.emptyList();
//		
//					
//		if (sortKeyList.isEmpty()) {
//			this.queryExpression = this.query;
//		}		
//		else {
//			OrderBy ob = te.getOrderBy();
//			
//			if (ob == null) {
//				ob = new OrderBy();
//				
//				for (EntityQuerySortKey<?> sk : sortKeyList) {
//					ColumnReference cr = sortKeyColumnMap.get(sk);										
//					// Only template attributes which are used as sort keys have associated column reference.
//					// Other sort keys just do without.					    					
//					ob.add(sk.sortKey(cr));
//				}
//			}
//			
//			for (ColumnReference pkcol : getRootPrimaryKey()) {
//				ob.add(pkcol, Order.ASC);
//			}
//
////			Limit le = (this.limit == null) ? null : new Limit(limit.longValue());
////			Offset oe = (this.offset == null) ? null : new Offset(this.offset.longValue());
//			
//			SelectStatement sq = new SelectStatement(te, ob, null, null);			
//			this.queryExpression = sq;
//		}
//		
////		logger().debug("init - exit");
//	}
//	
//	
//	
//	private <
//		QA extends com.appspot.relaxe.ent.Attribute,
//		QR extends com.appspot.relaxe.ent.Reference,
//		QT extends ReferenceType<QA, QR, QT, QE, QH, QF, QM, QC>,
//		QE extends Entity<QA, QR, QT, QE, QH, QF, QM, QC>,
//		QH extends ReferenceHolder<QA, QR, QT, QE, QH, QM, QC>,
//		QF extends EntityFactory<QE, QH, QM, QF, QC>,
//		QM extends EntityMetaData<QA, QR, QT, QE, QH, QF, QM, QC>,
//		QC extends com.appspot.relaxe.ent.Content,		
//		QX extends EntityQueryElement<QA, QR, QT, QE, QH, QF, QM, QC, QX>
//	>
//	
//	void addTemplatePredicates(EntityQueryElement<QA, QR, QT, QE, QH, QF, QM, QC, QX> qt, List<EntityQueryPredicate<?>> apl, Set<EntityQueryElement<?,?,?,?,?,?,?,?,?>> visited) {
//		// TODO: 
//		apl.addAll(qt.predicates());
//		
//		QM meta = qt.getMetaData();		
//		Set<QR> rs = meta.relationships();
//		
//		for (QR qr : rs) {
//			EntityKey<QA, QR, QT, QE, QH, QF, QM, QC, ?, ?, ?, ?, ?, ?, ?, ?, ?> k = meta.getEntityKey(qr);
//			EntityQueryElementTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> t = qt.getTemplate(k);
//			
//			if (t != null) {
//				if (!visited.contains(t)) {
//					visited.add(t);
//					addTemplatePredicates(t.self(), apl, visited);
//				}
//			}
//		}
//	}
//	
//	private <
//		QA extends Attribute,
//		QR extends Reference,
//		QT extends ReferenceType<QA, QR, QT, QE, QH, QF, QM, QC>,
//		QE extends Entity<QA, QR, QT, QE, QH, QF, QM, QC>,
//		QH extends ReferenceHolder<QA, QR, QT, QE, QH, QM, QC>,
//		QF extends EntityFactory<QE, QH, QM, QF, QC>,
//		QM extends EntityMetaData<QA, QR, QT, QE, QH, QF, QM, QC>,
//		QC extends Content,		
//		QX extends EntityQueryElement<QA, QR, QT, QE, QH, QF, QM, QC, QX>
//	>	
//	TableReference populateTableReferenceMap(
//			EntityQueryElementTemplate<QA, QR, QT, QE, QH, QF, QM, QC, QX> template, 
//			Map<EntityQueryElementTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, TableReference> rm,
//			Map<EntityQueryElementTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, Entry> rem) {
//		
////		logger().debug("populateTableReferenceMap - enter: " + template);
//		
//		try {		
//			TableReference tref = rm.get(template);
//			
//			if (tref != null) {
//				// already visited
//				return tref;			
//			}
//			
//			final QM meta = template.getMetaData();		
//			tref = new TableReference(meta.getBaseTable());		
//			rm.put(template, tref);
//			
////			logger().debug("put ref: " + template + " => " + tref);
//			
//			getMetaDataMap().put(tref, meta);
//			
//			Set<QR> rs = meta.relationships();
//			
////			logger().debug("populateTableReferenceMap: rs.size()=" + rs.size());
//			
//			for (QR qr : rs) {
//				EntityKey<QA, QR, QT, QE, QH, QF, QM, QC, ?, ?, ?, ?, ?, ?, ?, ?, ?> k = meta.getEntityKey(qr);
//				EntityQueryElementTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> t = template.getTemplate(k);
//				
////				logger().debug(qr.identifier() + " => template: " + tref);
//				
//				if (t != null) {								
//					TableReference rr = populateTableReferenceMap(t.self(), rm, rem);
//					
////					logger().debug(qr.identifier() + " => ref: " + rr);
//					
//					Entry e = rem.get(template);
//					
//					if (e == null) {
//						e = new Entry();
//						rem.put(template, e);
//					}
//					
//					ForeignKey fk = meta.getForeignKey(qr);				
//					e.getReferenceMap().put(fk, rr);
//					
//					JoinKey j = new JoinKey(tref, fk);
//					getReferenceMap().put(j, rr);	
//				}					
//			}
//			
//			return tref;
//		}
//		finally {
////			logger().debug("populateTableReferenceMap - exit");
//		}
//	}
//
//
//	private 
//	<
//		MA extends Attribute,
//		MR extends Reference,
//		MT extends ReferenceType<MA, MR, MT, ME, MH, MF, MM, MC>,
//		ME extends Entity<MA, MR, MT, ME, MH, MF, MM, MC>,
//		MH extends ReferenceHolder<MA, MR, MT, ME, MH, MM, MC>,		
//		MF extends EntityFactory<ME, MH, MM, MF, MC>,		
//		MM extends EntityMetaData<MA, MR, MT, ME, MH, MF, MM, MC>,
//		MC extends Content,		
//		MX extends EntityQueryElement<MA, MR, MT, ME, MH, MF, MM, MC, MX>
//	>
//	AbstractTableReference fromTemplate(
//			EntityQueryElementTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> parent,
//			EntityQueryElementTemplate<MA, MR, MT, ME, MH, MF, MM, MC, MX> template, 
//			AbstractTableReference qref, 
//			ForeignKey fk, 
//			TableReference referencing, 
//			DefaultTableExpression q,
//			Map<EntityQueryElementTemplate<?,?,?,?,?,?,?,?,?,?>, TableReference> visited,
//			Map<EntityQueryElementTemplate<?,?,?,?,?,?,?,?,?,?>, TableReference> rm,
//			Map<EntityQueryElementTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, Entry> rem)
//		throws EntityRuntimeException {
//		
////		logger().debug("fromTemplate - enter: " + template);
////		logger().debug("fk: " + fk);		
//		
//		if (visited.containsKey(template)) {
////			logger().debug("visited, exit...");
//			return qref;
//		}
//		
//		Select s = getSelect(q);
//		MM meta = template.getMetaData();
//		
//		final boolean root = (qref == null);
//		
//		final TableReference tref = rm.get(template);
//		
////		logger().debug("tref:  " + tref);
//						
//		visited.put(template, tref);
//				
//		if (qref == null) {
////			logger().debug("qref == null");
//			qref = tref;
//		}
//		else {
////			logger().debug("visible.size(): " + visited.keySet());
////			logger().debug("rem.size: " + rem.size());
////			logger().debug("rem keys: " + rem.keySet());
//						
//			List<CompoundJoinCondition.Component> jcl = null; 
//			
//			for (EntityQueryElementTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> vt : visited.keySet()) {
//				Entry e = rem.get(vt);
//				
////				logger().debug("has-entry: " + (e != null));
//				
//				if (e != null) {
//					Map<ForeignKey, TableReference> all = e.getReferenceMap();
//					
////					logger().debug("vref ref-map: " + all);
//				
//					for (Map.Entry<ForeignKey, TableReference> me : all.entrySet()) {
//						ForeignKey refk = me.getKey();
//						TableReference kref = me.getValue();
//						
//						if (kref == tref) {
//							if (jcl == null) {
//								jcl = new ArrayList<CompoundJoinCondition.Component>();
//							}
//							
//							TableReference lhs = visited.get(vt);
//							jcl.add(new CompoundJoinCondition.Component(lhs, refk, tref));
//						}
//					}
//				}
//			}
//			
////			logger().debug("fromTemplate: jcl.size(): " + ((jcl == null) ? -1 : jcl.size()));
//			
//			if (jcl != null) {
//				qref = join(qref, tref, template, referencing, jcl);
//			}
//		}
//					
//		Collection<Column> pkcols = meta.getBaseTable().getPrimaryKey().getColumnMap().values();
//		
//		for (Column c : pkcols) {
//			ColumnReference cref = new ColumnReference(tref, c);
//			s.add(cref);
//			
//			if (root) {
//				getRootPrimaryKey().add(cref);
//			}
//		}					
//
//		addAttributes(template, s, tref);
//				
//		List<EntityQueryPredicate<MA>> ps = template.predicates();
//		
//		for (EntityQueryPredicate<MA> k : ps) {
//			MA a = k.attribute();
//			ColumnReference cref = null;
//			
//			if (a != null) {			
//				Column c = meta.getColumn(a);
//				cref = new ColumnReference(tref, c);
//				predicateColumnMap.put(k, cref);				
//			}
//						
//			tableReferenceMap.put(k, tref);
//		}		
//		
//		// List<EntityQuerySortKey<MA>> keys = template.sortKeys();
//		// TODO: FIX REFACTORED
//		List<EntityQuerySortKey<MA>> keys = Collections.emptyList();
//		
//		for (EntityQuerySortKey<MA> k : keys) {
//			MA a = k.attribute();
//			ColumnReference cref = null;
//			
//			if (a != null) {			
//				Column c = meta.getColumn(a);
//				cref = new ColumnReference(tref, c);
//				sortKeyColumnMap.put(k, cref);
//			}
//		}
//		
//		getOriginMap().put(Integer.valueOf(s.getColumnCount()), tref);
//		
//		qref = processReferences(template, qref, tref, q, visited, rm, rem);
//		
////		logger().debug("fromTemplate - exit");
//						
//		return qref;
//	}
//	
//	/**
//	 * Defines how to join tables references <code>rhs</code> and <code>lhs</code>.
//	 *
//	 * 
//	 *   
//	 * @param rhs
//	 * @param lhs
//	 * @param referencing
//	 * @param krm
//	 * @return
//	 */
//	protected <
//		QT extends EntityQueryElementTemplate<?, ?, ?, ?, ?, ?, ?, ?, QT, ?>
//	>	
//	AbstractTableReference join(
//			AbstractTableReference lhs, TableReference rhs, 
//			EntityQueryElementTemplate<?, ?, ?, ?, ?, ?, ?, ?, QT, ?> template, TableReference referencing, 
//			List<CompoundJoinCondition.Component> jcl) {		
//		
//				
//		// MultiForeignKeyJoinCondition mc = new MultiForeignKeyJoinCondition(referencing, krm);
////		boolean inner = (krm.size() > 1 || template.getTemplateCount() > 0);
//		CompoundJoinCondition cc = new CompoundJoinCondition(jcl);
//				
//		boolean inner = (jcl.size() > 1 || template.getTemplateCount() > 0);
//		
//		AbstractTableReference joined = inner ? lhs.innerJoin(rhs, cc) : lhs.leftJoin(rhs, cc);
//		
//		return joined;
//	}
//	
//	
//
//	private	<
//		KA extends Attribute,
//		KR extends Reference,
//		KT extends ReferenceType<KA, KR, KT, KE, KH, KF, KM, KC>,
//		KE extends Entity<KA, KR, KT, KE, KH, KF, KM, KC>,
//		KH extends ReferenceHolder<KA, KR, KT, KE, KH, KM, KC>,
//		KF extends EntityFactory<KE, KH, KM, KF, KC>,
//		KM extends EntityMetaData<KA, KR, KT, KE, KH, KF, KM, KC>,
//		KC extends Content,
//		KQ extends EntityQueryElementTemplate<KA, KR, KT, KE, KH, KF, KM, KC, KQ, KX>,
//		KX extends EntityQueryElement<KA, KR, KT, KE, KH, KF, KM, KC, KQ, KX>
//	>
//	AbstractTableReference processReferences(
//			EntityQueryElementTemplate<KA, KR, KT, KE, KH, KF, KM, KC, KQ, KX> template, 
//			AbstractTableReference qref, 
//			TableReference tref, 
//			DefaultTableExpression q, 
//			Map<EntityQueryElementTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, TableReference> visited, 
//			Map<EntityQueryElementTemplate<?,?,?,?,?,?,?,?,?,?>, TableReference> rm,
//			Map<EntityQueryElementTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, Entry> rem)
//		throws EntityRuntimeException {
//		
////		logger().debug("processReferences - enter");		
//
//		KM meta = template.getMetaData();		
//		Set<KR> rs = meta.relationships();
//		
//		for (KR kr : rs) {
//			EntityKey<KA, KR, KT, KE, KH, KF, KM, KC, ?, ?, ?, ?, ?, ?, ?, ?, ?> ek = meta.getEntityKey(kr);
//			EntityQueryElementTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> t = template.getTemplate(ek);
//			
////			logger().debug("ref-template for: " + ek + " => " + t);
//			
//			if (t != null) {
//				ForeignKey fk = meta.getForeignKey(kr);
//
//				if (fk == null) {
//					throw new NullPointerException("can not find fk by relationship: " + kr);
//				}
//				
//				qref = fromTemplate(template.self(), t.self(), qref, fk, tref, q, visited, rm, rem);
//			}									
//		}
//
//		logger().debug("processReferences - exit");
//		
//		return qref;
//	
//	}
//
//	private Select getSelect(DefaultTableExpression q) {
//		Select s = q.getSelect();
//
//		if (s == null) {
//			q.setSelect(s = new Select());
//		}
//		
//		return s;
//	}
//
//	private <
//		MA extends Attribute,
//		D extends Entity<MA, ?, ?, D, ?, ?, DM, ?>,
//		DM extends EntityMetaData<MA, ?, ?, D, ?, ?, DM, ?>,
//		DQ extends EntityQueryElementTemplate<MA, ?, ?, D, ?, ?, DM, ?, DQ, ?>
//	> 
//	void addAttributes(EntityQueryElementTemplate<MA, ?, ?, D, ?, ?, DM, ?, DQ, ?> template, Select s, TableReference tref) throws EntityRuntimeException {
//		DM meta = template.getMetaData();
//		Set<MA> as = meta.attributes();
//		BaseTable t = meta.getBaseTable();
//								
//		for (MA a : as) {
//			EntityQueryTemplateAttribute ta = template.get(a);
//			
//			if (ta == null) {
//				continue;
//			}
//			
//			Column c = meta.getColumn(a);
//			
//			if (t.isPrimaryKeyColumn(c)) {
//				continue;
//			}			
//			
//			ColumnReference cref = new ColumnReference(tref, c);
//			
//			getColumnMap().put(ta, cref);
//			
//			if (ta.isSelected(cref)) {
//				s.add(cref);
//			}
//			
//			Predicate p = ta.createPredicate(cref);			
//			addPredicate(p);
//		}
//	}
//
//	private boolean addPredicate(Predicate p) {
//		if (p == null) {
//			return false;
//		}
//		
//		return getPredicateList().add(p);
//	}
//	
//	@Override
//	public DefaultTableExpression getTableExpression() 
//		throws EntityRuntimeException {
//		init();		
//		return this.query;
//	}
//
//	@Override
//	public QueryExpression getQueryExpression() 
//		throws EntityRuntimeException {		
//		init();		
//		return this.queryExpression;
//	}
//	
//	/**
//	 * Returns the root table-reference for this query.
//	 * @return
//	 */
//	@Override
//    public TableReference getTableRef() {
//        if (this.rootRef == null) {
//        	// init sets all the tables references at once, including root.
//        	init();
//        }
//
//        return this.rootRef;
//    }
//
////	@Override
////	public Long getLimit() {
////		return null;
////	}
////
////	@Override
////	public int getOffset() {
////		return 0;
////	}
//
////	@Override
////	public M getMetaData() {
////		return this.type.getMetaData();		
////	}
//
//	@Override
//	public EntityMetaData<?, ?, ?, ?, ?, ?, ?, ?> getMetaData(TableReference tref) 
//		throws EntityRuntimeException {
//		if (tref == null) {
//			throw new NullPointerException("tref");
//		}
//		
//		init();
//		
//		return getMetaDataMap().get(tref);
//	}
//
//	/**
//	 * Returns the table reference which the specified <code>column</code> originates from.
//	 * Column numbering starts from 1.
//	 * 
//	 * Throws {@link IndexOutOfBoundsException} if column &lt; 1. 
//	 */	
//	@Override
//	public TableReference getOrigin(int column) 
//		throws EntityRuntimeException {	
//		if (column < 1) {
//			throw new IndexOutOfBoundsException();
//		}
//		
//		init();
//		
//		Map<Integer, TableReference> om = getOriginMap();
//				
//		for (Integer k : om.keySet()) {
//			if (column <= k.intValue()) {
//				return om.get(k);
//			}
//		}	
//		
//		return null;
//	}
//
//	private Map<TableReference, EntityMetaData<?, ?, ?, ?, ?, ?, ?, ?>> getMetaDataMap() {
//		if (metaDataMap == null) {
//			metaDataMap = new HashMap<TableReference, EntityMetaData<?,?,?,?,?,?,?,?>>();			
//		}
//
//		return metaDataMap;
//	}
//		
//	@Override
//	public TableReference getReferenced(TableReference referencing, ForeignKey fk) {
//		if (referencing == null) {
//			return null;
//		}
//		
//		JoinKey k = new JoinKey(referencing, fk);
//		TableReference r = getReferenceMap().get(k);
//		return r;
//	}
//
//	private static class JoinKey
//		implements Serializable {
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = -2839759478114689320L;
//		private TableReference referencing;	
//		private ForeignKey foreignKey;
//		
//		/**
//		 * No-argument constructor for GWT Serialization
//		 */
//		@SuppressWarnings("unused")
//		private JoinKey() {	
//		}
//		
//		public JoinKey(TableReference referencing, ForeignKey foreignKey) {
//			super();
//			this.referencing = referencing;			
//			this.foreignKey = foreignKey;
//		}
//		
//		@Override
//		public int hashCode() {		
//			return referencing.hashCode() ^ foreignKey.hashCode();
//		}
//		
//		@Override
//		public boolean equals(Object obj) {
//			if (obj == null) {
//				throw new NullPointerException("obj");
//			}
//						
//			JoinKey j = (JoinKey) obj;
//			
//			return 
//				this.referencing.equals(j.referencing) &&
//				this.foreignKey.equals(j.foreignKey);
//		}
//	}
//
////	private static Logger logger() {
////		return DefaultEntityQuery.logger;
////	}
//	
//
//	private List<Predicate> getPredicateList() {
//		if (predicateList == null) {
//			predicateList = new ArrayList<Predicate>();
//			
//		}
//
//		return predicateList;
//	}
//	
//	
////	private List<EntityQuerySortKey> getSortKeyList() {
////		if (sortKeyList == null) {
////			sortKeyList = new ArrayList<EntityQuerySortKey>();			
////		}
////
////		return sortKeyList;
////	}
//	
////	private boolean addSortKey(EntityQuerySortKey sk) {
////		if (sk == null) {
////			return false;
////		}
////		
////		return getSortKeyList().add(sk);
////	}
//	
//	private List<ColumnReference> getRootPrimaryKey() {
//		if (rootPrimaryKey == null) {
//			rootPrimaryKey = new ArrayList<ColumnReference>();			
//		}
//
//		return rootPrimaryKey;
//	}
//	
////	@Override
////	public RT getTemplate() {
////		return template;
////	}
//	
//	private Map<Integer, TableReference> getOriginMap() {
//		if (originMap == null) {
//			originMap = new LinkedHashMap<Integer, TableReference>();			
//		}
//
//		return originMap;
//	}
//	
//	
//	private Map<JoinKey, TableReference> getReferenceMap() {
//		if (referenceMap == null) {
//			referenceMap = new HashMap<JoinKey, TableReference>();
//		}
//
//		return referenceMap;		
//	}
//	
//	private boolean isInitialized() {
//		return (this.queryExpression != null);
//	}
//		
//	public Map<EntityQueryTemplateAttribute, ColumnReference> getColumnMap() {
//		if (columnMap == null) {
//			columnMap = new HashMap<EntityQueryTemplateAttribute, ColumnReference>();			
//		}
//
//		return columnMap;
//	}	
//	
//	public Logger logger() {
//		return DefaultLogger.getLogger();
//	}
//
//	@Override
//	public RE getRootElement() {
//		return null;
//	}
//
//	@Override
//	public TableReference getTableRef(EntityQueryElementTag tag) {
//		return null;
//	}
//
//	@Override
//	public TableReference getTableReference(EntityQueryElementTag etag)
//			throws EntityRuntimeException {
//		return null;
//	}
}

