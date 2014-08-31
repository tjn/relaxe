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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQueryContext;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.EntityQueryElementTag;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.ent.Operation;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.value.EntityKey;
import com.appspot.relaxe.expr.AbstractRowValueConstructor;
import com.appspot.relaxe.expr.ColumnReference;
import com.appspot.relaxe.expr.ElementList;
import com.appspot.relaxe.expr.ImmutableValueParameter;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.RowValueConstructor;
import com.appspot.relaxe.expr.RowValueConstructorElement;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.expr.op.In;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ColumnMap;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.ReferenceHolder;
import com.appspot.relaxe.value.ValueHolder;

public abstract class EntityQueryInPredicate
	implements EntityQueryPredicate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4578460236556406293L;

	public static class Referenced<
		A extends AttributeName,
		R extends Reference,	
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>,
		QE extends EntityQueryElement<A, R, T, E, B, H, F, M, QE>,
		RA extends AttributeName,
		RR extends Reference,	
		RT extends ReferenceType<RA, RR, RT, RE, RB, RH, RF, RM>,
		RE extends Entity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RB extends MutableEntity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
		RF extends EntityFactory<RE, RB, RH, RM, RF>,
		RM extends EntityMetaData<RA, RR, RT, RE, RB, RH, RF, RM>,
		K extends EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, K>
	>
		extends EntityQueryInPredicate {
	
		/**
		 * 
		 */
		private static final long serialVersionUID = -9214275181479579908L;
			
		private QE left;
		private K key; 
		private Collection<RE> keys;
		
		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private Referenced() {
		}
			
		public Referenced(QE left, K key, Collection<RE> entities) {
			super();
			
			if (left == null) {
				throw new NullPointerException("left");
			}		
			
			if (key == null) {
				throw new NullPointerException("key");
			}
			
			this.key = key;
			this.left = left;
			this.keys = getPrimaryKeys(entities);
		}
		
		@Override
		public Predicate predicate(EntityQueryContext ctx) {			
			return newPredicate(ctx, left, key, this.keys);
		}		
	}
	
	
	public static class Referencing<
		A extends AttributeName,
		R extends Reference,	
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,		
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>,
		RA extends AttributeName,
		RR extends Reference,	
		RT extends ReferenceType<RA, RR, RT, RE, RB, RH, RF, RM>,
		RE extends Entity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RB extends MutableEntity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
		RF extends EntityFactory<RE, RB, RH, RM, RF>,
		RM extends EntityMetaData<RA, RR, RT, RE, RB, RH, RF, RM>,	
		QE extends EntityQueryElement<RA, RR, RT, RE, RB, RH, RF, RM, QE>,
		K extends EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, K>
	>
		extends EntityQueryInPredicate {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -9214275181479579908L;
			
		private QE left;
		private K key; 
		private Collection<RE> keys;
		
		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private Referencing() {
		}
			
		public Referencing(QE left, K key, Collection<E> entities) {
			super();
			
			if (left == null) {
				throw new NullPointerException("left");
			}		
			
			if (key == null) {
				throw new NullPointerException("key");
			}
			
			this.key = key;
			this.left = left;
			this.keys = getForeignKeys(key, entities);				
		}
	
		@Override
		public Predicate predicate(EntityQueryContext ctx) {
			return newPredicate(ctx, left, key, this.keys);
		}			
		
		Collection<RE> getForeignKeys(K key, Collection<E> entities) {
			if (entities == null) {
				throw new NullPointerException("entities");
			}
			
			if (entities.isEmpty()) {
				throw new IllegalArgumentException("'entities' must not be empty here");
			}
			
			
			Operation op = new Operation();
			
			try {
				Set<RE> kl = new HashSet<RE>(entities.size());
				
				for (E e : entities) {
					RH rh = key.get(e);
					RE re = (rh == null) ? null : rh.value();
					
					if (re == null) {
						continue;
					}
					
					RE pk = re.toPrimaryKey(op.getContext());
					
					if (pk == null) {
						throw new EntityRuntimeException("primary key required", re);
					}
					
					kl.add(pk);
				}
				
				return kl;
			}
			finally {
				op.finish();
			}
		}	
	}
	
	<
		E extends Entity<?, ?, ?, E, ?, ?, ?, ?>
	> 
	List<E> getPrimaryKeys(Collection<E> entities) {
		if (entities == null) {
			throw new NullPointerException("entities");
		}
		
		if (entities.isEmpty()) {
			throw new IllegalArgumentException("'entities' must not be empty here");
		}		
		
		List<E> kl = new ArrayList<E>(entities.size());
		
		
		Operation op = new Operation();
		
		try {							
			for (E e : entities) {
				E pk = e.toPrimaryKey(op.getContext());
				
				if (pk == null) {
					throw new EntityRuntimeException("primary key required", e);
				}
				
				kl.add(pk);
			}
		}
		finally {
			op.finish();
		}
		
		return kl;
	}
	
	
	
	

	
	/**
	 * TODO: merge with similar code in PersistenceManager
	 * @param col
	 * @param holder
	 * @return
	 */
	
	<		
		PV extends Serializable,
		PT extends ValueType<PT>,
		PH extends ValueHolder<PV, PT, PH>
	>
	ImmutableValueParameter<PV, PT, PH> newRowValueConstructorElement(Column col, ValueHolder<PV, PT, PH> holder) {		
		return new ImmutableValueParameter<PV, PT, PH>(col, holder.self());
	}






	<
		A extends AttributeName,
		R extends Reference,	
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,		
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>,
		RA extends AttributeName,
		RR extends Reference,	
		RT extends ReferenceType<RA, RR, RT, RE, RB, RH, RF, RM>,
		RE extends Entity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RB extends MutableEntity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
		RF extends EntityFactory<RE, RB, RH, RM, RF>,
		RM extends EntityMetaData<RA, RR, RT, RE, RB, RH, RF, RM>,
		K extends EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, K>
	> 
	Predicate newPredicate(EntityQueryContext ctx, EntityQueryElementTag left, K key, Collection<RE> pks) {
		TableReference lref = ctx.getTableRef(left);
		
		ForeignKey fk = key.getSource().getForeignKey(key.name());
		ColumnMap cm = fk.getColumnMap();
		
		int cc = cm.size();
		
		ColumnMap pkcm = key.getTarget().getBaseTable().getPrimaryKey().getColumnMap();
		
		List<RowValueConstructorElement> lhs = new ArrayList<RowValueConstructorElement>(cc);		
				
		int[] indexes = new int[cc];
		Column[] rclist = new Column[cc];
		 		
		for (int i = 0; i < cc; i++) {
			Column col = cm.get(i);
			lhs.add(new ColumnReference(lref, col));									
			Column rc = fk.getReferenced(col);			
			indexes[i] = pkcm.indexOf(rc.getColumnName());
			rclist[i] = rc;
		}		
				
		final RowValueConstructor rvc = AbstractRowValueConstructor.of(lhs);				
		List<RowValueConstructor> rows = new ArrayList<RowValueConstructor>();
		List<RowValueConstructorElement> values = new ArrayList<RowValueConstructorElement>();
						
		for (RE t : pks) {
			values.clear();			
			
			int i = 0;
			
			for (Column rc : rclist) {				
				ValueHolder<?, ?, ?> vh = t.get(rc);								
				RowValueConstructorElement e = newRowValueConstructorElement(rc, vh);				
				values.add(e);
				i++;
			}			
			
			RowValueConstructor vc = AbstractRowValueConstructor.of(values);
			rows.add(vc);
		}
		
		ElementList<RowValueConstructor> rhs = ElementList.newElementList(rows);						
		In in = new In(rvc, rhs);
		
		return in;		
	}	
	
}