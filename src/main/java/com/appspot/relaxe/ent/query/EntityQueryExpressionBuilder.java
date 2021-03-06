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
package com.appspot.relaxe.ent.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityException;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQuery;
import com.appspot.relaxe.ent.EntityQueryContext;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.EntityQueryElementTag;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.value.EntityKey;
import com.appspot.relaxe.expr.AbstractTableReference;
import com.appspot.relaxe.expr.ColumnReference;
import com.appspot.relaxe.expr.CompoundJoinCondition;
import com.appspot.relaxe.expr.DefaultTableExpression;
import com.appspot.relaxe.expr.From;
import com.appspot.relaxe.expr.OrderBy;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.expr.Select;
import com.appspot.relaxe.expr.SelectStatement;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.expr.Where;
import com.appspot.relaxe.expr.op.AndPredicate;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;

public class EntityQueryExpressionBuilder<
	A extends AttributeName,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, B, H, F, M>,
	E extends Entity<A, R, T, E, B, H, F, M>,
	B extends MutableEntity<A, R, T, E, B, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, B, H, M, F>,
	M extends EntityMetaData<A, R, T, E, B, H, F, M>,
	RE extends EntityQueryElement<A, R, T, E, B, H, F, M, RE>
>
	implements EntityQueryContext {
	
	private EntityQuery<A, R, T, E, B, H, F, M, RE> query;		
	private QueryExpression result;
	private LinkedHashMap<Integer, TableReference> originMap;
	
	private Map<EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?>, TableReference> tableReferenceMap;
	private Map<EntityQueryElementTag, Entry> entryMap;
	private Map<JoinKey, TableReference> referenceMap;
		
	private EntityQueryContext parent;
	
	private List<EntityQueryColumn<?, ?, ?, ?, ?, ?, ?, ?, ?>> columnList;
	
	private static final class Entry {				
		private Map<ForeignKey, TableReference> referenceMap;
		
		public Entry() {
			super();			
		}
		
		public Map<ForeignKey, TableReference> getReferenceMap() {
			if (referenceMap == null) {
				referenceMap = new HashMap<ForeignKey, TableReference>();				
			}

			return referenceMap;
		}
	}
	
	public EntityQueryExpressionBuilder(EntityQuery<A, R, T, E, B, H, F, M, RE> query) {
		this(null, query);
		
	}
	public EntityQueryExpressionBuilder(EntityQueryContext parent, EntityQuery<A, R, T, E, B, H, F, M, RE> query) {
		super();
		this.parent = parent;
		this.query = query;
	}

	public QueryExpression getQueryExpression() {
		if (this.result == null) {
			build(false);
		}		
		
		return result;
	}
	
	private boolean isBuilt() {
		return (this.result != null);
	}
	
	private void build(boolean force) {
		if (force || (!isBuilt())) {
			build();
		}
	}
	
	@Override
	public EntityQuery<A, R, T, E, B, H, F, M, RE> getQuery() {
		return query;
	}

	private void build() {
			
		RE root = this.query.getRootElement();
		
		populateTableReferenceMap();
		
		Map<EntityQueryElementTag, TableReference> visited = new HashMap<EntityQueryElementTag, TableReference>();
		AbstractTableReference tref = createTableReference(null, root, null, null, null, visited);
				
		Select.Builder sb = new Select.Builder();
		
		addAttributes(sb);
		From f = new From(tref);
		
		Predicate p = createPredicate();
		Where w = (p == null) ? null : new Where(p);
		
		Select s = sb.newSelect();
				
		DefaultTableExpression te = new DefaultTableExpression(s, f, w, null);
		
		OrderBy ob = createOrderBy();
		SelectStatement ss = new SelectStatement(te, ob, null, null);
		
		this.result = ss;
	}

	private void addAttributes(Select.Builder selectBuilder) {
		int cc = 0;
		
		this.columnList = new ArrayList<EntityQueryColumn<?, ?, ?, ?, ?, ?, ?, ?, ?>>();
		
		for (Map.Entry<EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?>, TableReference> e : this.tableReferenceMap.entrySet()) {
			int nc = addAttributes(e.getKey(), e.getValue(), selectBuilder, cc);
			cc += nc;
		}
	}
	
	private OrderBy createOrderBy() {
		List<EntityQuerySortKey> skl = this.getQuery().sortKeys();
		
		if (skl == null || skl.isEmpty()) {
			return null;
		}
		
		EntityQueryContext ctx = this;
		
		OrderBy.Builder obb = new OrderBy.Builder();
		
		for (EntityQuerySortKey esk : skl) {
			OrderBy.SortKey sk = esk.sortKey(ctx);
			obb.add(sk);
		}		
		
		OrderBy ob = obb.newOrderBy();
		return ob;
	}

	private Predicate createPredicate() {		
		List<EntityQueryPredicate> pl = null;
		
		pl = append(pl, query.predicates());

		for (EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?> e : tableReferenceMap.keySet()) {
			pl = append(pl, e.predicates());
		}
		
		Predicate p = null;
		
		if (pl == null) {
			return p;
		}
		
		EntityQueryContext ctx = this;
				
		{
			EntityQueryPredicate ep = pl.get(0);
			p = ep.predicate(ctx);
		}
				
		int pc = pl.size();
		
		if (pc == 1) {			
			return p; 
		}
		
		for (int i = 1; i < pc; i++) {
			EntityQueryPredicate ep = pl.get(i);
			Predicate np = ep.predicate(ctx);
			p = AndPredicate.newAnd(p, np);
		}
		
		return p;
	}


	private List<EntityQueryPredicate> append(List<EntityQueryPredicate> dest, Collection<EntityQueryPredicate> src) {
		if (src == null || src.isEmpty()) {
			return dest;
		}
		
		if (dest == null) {
			dest = new ArrayList<EntityQueryPredicate>();
		}
		
		dest.addAll(src);
		
		return dest;
	}

	private void populateTableReferenceMap() {
		this.tableReferenceMap = new LinkedHashMap<EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?>, TableReference>();
		this.entryMap = new HashMap<EntityQueryElementTag, Entry>();
		
		RE root = this.query.getRootElement();		
		populateTableReferenceMap(root);
				
		
	}
	
	public TableReference getRootRef() {
		if (this.tableReferenceMap == null) {
			build(false);	
		}		
		
		return this.tableReferenceMap.get(this.query.getRootElement());
	}
	
	
	private <
		XA extends AttributeName,
		XR extends Reference,
		XT extends ReferenceType<XA, XR, XT, XE, XB, XH, XF, XM>,
		XE extends Entity<XA, XR, XT, XE, XB, XH, XF, XM>,
		XB extends MutableEntity<XA, XR, XT, XE, XB, XH, XF, XM>,
		XH extends ReferenceHolder<XA, XR, XT, XE, XH, XM>,
		XF extends EntityFactory<XE, XB, XH, XM, XF>,
		XM extends EntityMetaData<XA, XR, XT, XE, XB, XH, XF, XM>,
		XRE extends EntityQueryElement<XA, XR, XT, XE, XB, XH, XF, XM, XRE>
	>	
	TableReference populateTableReferenceMap(EntityQueryElement<XA, XR, XT, XE, XB, XH, XF, XM, XRE> element) {
		Map<EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?>, TableReference> rm = this.tableReferenceMap;
					
		TableReference tref = rm.get(element);
		
		if (tref != null) {
			return tref;
		}
		
		XM meta = element.getMetaData();
		
		tref = new TableReference(meta.getBaseTable());
		rm.put(element, tref);		
		Set<XR> rs = meta.relationships();
		
		for (XR xref : rs) {
			EntityKey<XA, XR, XT, XE, XB, XH, XF, XM, ?, ?, ?, ?, ?, ?, ?, ?, ?> key = meta.getEntityKey(xref);
			EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?> ce = element.getQueryElement(key);
			
			if (ce != null) {
				TableReference rr = populateTableReferenceMap(ce);
				
				Entry e = this.entryMap.get(element);
				
				if (e == null) {
					e = new Entry();
					this.entryMap.put(element, e);
				}
				
				ForeignKey fk = meta.getForeignKey(xref);				
				e.getReferenceMap().put(fk, rr);
				
				JoinKey j = new JoinKey(tref, fk);
				getReferenceMap().put(j, rr);						
			}
		}
		
		return tref;
	}

	private <
		XA extends AttributeName,
		XR extends Reference,
		XT extends ReferenceType<XA, XR, XT, XE, XB, XH, XF, XM>,
		XE extends Entity<XA, XR, XT, XE, XB, XH, XF, XM>,
		XB extends MutableEntity<XA, XR, XT, XE, XB, XH, XF, XM>,
		XH extends ReferenceHolder<XA, XR, XT, XE, XH, XM>,
		XF extends EntityFactory<XE, XB, XH, XM, XF>,
		XM extends EntityMetaData<XA, XR, XT, XE, XB, XH, XF, XM>,
		XRE extends EntityQueryElement<XA, XR, XT, XE, XB, XH, XF, XM, XRE>
	>	
	int addAttributes(EntityQueryElement<XA, XR, XT, XE, XB, XH, XF, XM, XRE> element, TableReference tref, Select.Builder selectBuilder, final int column) {
				
		int added = 0;
		
		Set<XA> as = element.attributes();
		
		if (!as.isEmpty()) {
			XM meta = element.getMetaData();
			
			Map<Integer, TableReference> om = getOriginMap();
			
			for (XA xa : element.attributes()) {
				if (tref == null) {
					throw new NullPointerException("tref");
				}				
				
				Column col = meta.getColumn(xa);
				ColumnReference cref = new ColumnReference(tref, col);
				added++;
				
				int ordinal = column + added;
				this.columnList.add(new EntityQueryColumn<XA, XR, XT, XE, XB, XH, XF, XM, XRE>(element.self(), xa, ordinal));
								
				om.put(Integer.valueOf(ordinal), tref);
				selectBuilder.add(cref);
			}
		}
		
		return added;
	}
	
	private Map<JoinKey, TableReference> getReferenceMap() {
		if (referenceMap == null) {
			referenceMap = new HashMap<JoinKey, TableReference>();
		}

		return referenceMap;		
	}	
	

	@Override
	public TableReference getTableRef(EntityQueryElementTag tag) {
		if (tableReferenceMap == null) {
			build(false);	
		}				
				 
		TableReference tref = tableReferenceMap.get(tag);
				
		if (tref == null && parent != null) {
			tref = parent.getTableRef(tag);
		}
		
		return tref;
	}
	
	public<
		XA extends AttributeName,
		XR extends Reference,
		XT extends ReferenceType<XA, XR, XT, XE, XB, XH, XF, XM>,
		XE extends Entity<XA, XR, XT, XE, XB, XH, XF, XM>,
		XB extends MutableEntity<XA, XR, XT, XE, XB, XH, XF, XM>,
		XH extends ReferenceHolder<XA, XR, XT, XE, XH, XM>,
		XF extends EntityFactory<XE, XB, XH, XM, XF>,
		XM extends EntityMetaData<XA, XR, XT, XE, XB, XH, XF, XM>,
		XRE extends EntityQueryElement<XA, XR, XT, XE, XB, XH, XF, XM, XRE>
	>
	int findColumn(XRE element, XA attribute) {
		if (this.columnList == null) {
			build(false);
		}
		
		int result = -1;
		int colno = 1;
						
		for (EntityQueryColumn<?, ?, ?, ?, ?, ?, ?, ?, ?> qc : this.columnList) {
			if (qc.getQueryElement().equals(element) && qc.getAttribute().equals(attribute)) {
				result = colno;
				break;
			}
			
			colno++;
		}
		
		return result;
	}

	@Override
	public TableReference getReferenced(TableReference referencing, ForeignKey fk) throws EntityException {
		if (this.referenceMap == null) {
			build(false);
		}
		
		if (referencing == null) {
			return null;
		}
		
		JoinKey jk = new JoinKey(referencing, fk);		
		TableReference referenced = getReferenceMap().get(jk);		
		return referenced;
	}
	

	private Map<Integer, TableReference> getOriginMap() {
		if (originMap == null) {
			originMap = new LinkedHashMap<Integer, TableReference>();			
		}

		return originMap;
	}
	
	@Override
	public TableReference getOrigin(int column) 
			throws EntityRuntimeException {	
		if (column < 1) {
			throw new IndexOutOfBoundsException();
		}
		
		if (this.originMap == null) {
			build(false);	
		}
		
		Map<Integer, TableReference> om = getOriginMap();
				
		for (Integer k : om.keySet()) {
			if (column <= k.intValue()) {
				return om.get(k);
			}
		}	
		
		return null;
	}
	
	
	private 
	<
		MA extends AttributeName,
		MR extends Reference,
		MT extends ReferenceType<MA, MR, MT, ME, MB, MH, MF, MM>,
		ME extends Entity<MA, MR, MT, ME, MB, MH, MF, MM>,
		MB extends MutableEntity<MA, MR, MT, ME, MB, MH, MF, MM>,
		MH extends ReferenceHolder<MA, MR, MT, ME, MH, MM>,		
		MF extends EntityFactory<ME, MB, MH, MM, MF>,		
		MM extends EntityMetaData<MA, MR, MT, ME, MB, MH, MF, MM>,		
		MX extends EntityQueryElement<MA, MR, MT, ME, MB, MH, MF, MM, MX>
	>
	AbstractTableReference createTableReference(
			EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?> parent,
			EntityQueryElement<MA, MR, MT, ME, MB, MH, MF, MM, MX> element, 
			AbstractTableReference qref, 
			ForeignKey fk, 
			TableReference referencing,
			Map<EntityQueryElementTag, TableReference> visited)
		throws EntityRuntimeException {
		
//		logger().debug("fromTemplate - enter: " + template);
//		logger().debug("fk: " + fk);		
		
		if (visited.containsKey(element)) {
//			logger().debug("visited, exit...");
			return qref;
		}
				
		// MM meta = element.getMetaData();		
		// final boolean root = (qref == null);
		
		final TableReference tref = this.tableReferenceMap.get(element);
		
//		logger().debug("tref:  " + tref);
						
		visited.put(element, tref);
				
		if (qref == null) {
//			logger().debug("qref == null");
			qref = tref;
		}
		else {
//			logger().debug("visible.size(): " + visited.keySet());
//			logger().debug("rem.size: " + rem.size());
//			logger().debug("rem keys: " + rem.keySet());
						
			List<CompoundJoinCondition.Component> jcl = null; 
			
			for (EntityQueryElementTag vt : visited.keySet()) {
				Entry e = this.entryMap.get(vt);
				
//				logger().debug("has-entry: " + (e != null));
				
				if (e != null) {
					Map<ForeignKey, TableReference> all = e.getReferenceMap();
					
//					logger().debug("vref ref-map: " + all);
				
					for (Map.Entry<ForeignKey, TableReference> me : all.entrySet()) {
						ForeignKey refk = me.getKey();
						TableReference kref = me.getValue();
						
						if (kref == tref) {
							if (jcl == null) {
								jcl = new ArrayList<CompoundJoinCondition.Component>();
							}
							
							TableReference lhs = visited.get(vt);
							jcl.add(new CompoundJoinCondition.Component(lhs, refk, tref));
						}
					}
				}
			}
			
//			logger().debug("fromTemplate: jcl.size(): " + ((jcl == null) ? -1 : jcl.size()));
			
			if (jcl != null) {
				qref = join(qref, tref, element, referencing, jcl);
			}
		}
					
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
		
		qref = processReferences(element, qref, tref, visited);
		
//		logger().debug("fromTemplate - exit");
						
		return qref;
	}	
	
	private	<
		KA extends AttributeName,
		KR extends Reference,
		KT extends ReferenceType<KA, KR, KT, KE, KB, KH, KF, KM>,
		KE extends Entity<KA, KR, KT, KE, KB, KH, KF, KM>,
		KB extends MutableEntity<KA, KR, KT, KE, KB, KH, KF, KM>,
		KH extends ReferenceHolder<KA, KR, KT, KE, KH, KM>,
		KF extends EntityFactory<KE, KB, KH, KM, KF>,
		KM extends EntityMetaData<KA, KR, KT, KE, KB, KH, KF, KM>,		
		KX extends EntityQueryElement<KA, KR, KT, KE, KB, KH, KF, KM, KX>
	>
	AbstractTableReference processReferences(
			EntityQueryElement<KA, KR, KT, KE, KB, KH, KF, KM, KX> template, 
			AbstractTableReference qref, 
			TableReference tref,			 
			Map<EntityQueryElementTag, TableReference> visited)
		throws EntityRuntimeException {
		
	//	logger().debug("processReferences - enter");		
	
		KM meta = template.getMetaData();		
		Set<KR> rs = meta.relationships();
		
		for (KR kr : rs) {
			EntityKey<KA, KR, KT, KE, KB, KH, KF, KM, ?, ?, ?, ?, ?, ?, ?, ?, ?> ek = meta.getEntityKey(kr);
			EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?> t = template.getQueryElement(ek);
						
	//		logger().debug("ref-template for: " + ek + " => " + t);
			
			if (t != null) {
				ForeignKey fk = meta.getForeignKey(kr);
	
				if (fk == null) {
					throw new NullPointerException("can not find fk by relationship: " + kr);
				}
				
				qref = createTableReference(template.self(), t.self(), qref, fk, tref, visited);
			}									
		}
	
		// logger().debug("processReferences - exit");
		
		return qref;	
	}	
	
	public List<EntityQueryColumn<?, ?, ?, ?, ?, ?, ?, ?, ?>> getColumnList() {
		if (this.columnList == null) {
			build(false);
		}

		return Collections.unmodifiableList(this.columnList);
	}

	private static class JoinKey
		implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2839759478114689320L;
		private TableReference referencing;	
		private ForeignKey foreignKey;
		
		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private JoinKey() {	
		}
		
		public JoinKey(TableReference referencing, ForeignKey foreignKey) {
			super();
			this.referencing = referencing;			
			this.foreignKey = foreignKey;
		}
		
		@Override
		public int hashCode() {		
			return referencing.hashCode() ^ foreignKey.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				throw new NullPointerException("obj");
			}
						
			JoinKey j = (JoinKey) obj;
			
			return 
				this.referencing.equals(j.referencing) &&
				this.foreignKey.equals(j.foreignKey);
		}
	}

	/**
	 * Defines how to join tables references <code>rhs</code> and <code>lhs</code>.
	 *
	 * 
	 *   
	 * @param rhs
	 * @param lhs
	 * @param referencing
	 * @param krm
	 * @return
	 */
	protected <
		QE extends EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, QE>
	>	
	AbstractTableReference join(
			AbstractTableReference lhs, TableReference rhs, 
			EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, QE> element, TableReference referencing, 
			List<CompoundJoinCondition.Component> jcl) {		
		
				
		// MultiForeignKeyJoinCondition mc = new MultiForeignKeyJoinCondition(referencing, krm);
//		boolean inner = (krm.size() > 1 || template.getTemplateCount() > 0);
		CompoundJoinCondition cc = new CompoundJoinCondition(jcl);
				
		boolean inner = (jcl.size() > 1 || element.getElementCount() > 0);
		
		AbstractTableReference joined = inner ? lhs.innerJoin(rhs, cc) : lhs.leftJoin(rhs, cc);
		
		return joined;
	}
	
}