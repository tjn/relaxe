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

import java.util.ArrayList;
import java.util.Collection;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQueryContext;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.value.EntityKey;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.expr.op.Comparison;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;

public class EntityQueryInPredicate<
	A extends AttributeName,
	R extends Reference,	
	T extends ReferenceType<A, R, T, E, H, F, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>,
	QE extends EntityQueryElement<A, R, T, E, H, F, M, QE>,
	RA extends AttributeName,
	RR extends Reference,	
	RT extends ReferenceType<RA, RR, RT, RE, RH, RF, RM>,
	RE extends Entity<RA, RR, RT, RE, RH, RF, RM>,
	RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
	RF extends EntityFactory<RE, RH, RM, RF>,
	RM extends EntityMetaData<RA, RR, RT, RE, RH, RF, RM>,
	K extends EntityKey<A, R, T, E, H, F, M, RA, RR, RT, RE, RH, RF, RM, K>
>
	implements EntityQueryPredicate {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9214275181479579908L;
	
	private Comparison.Op op;
	private QE left;
	private Collection<RE> right;
	private K key; 
		
	public EntityQueryInPredicate(Comparison.Op op, QE left, K key, Collection<RE> entities) {
		super();
		this.op = op;
		this.key = key;
		this.left = left;
		this.right = entities;
		
		if (entities == null) {
			throw new NullPointerException("entities");
		}
		
		if (entities.isEmpty()) {
			throw new IllegalArgumentException("'entities' must not be empty here");
		}
		
		this.right = new ArrayList<RE>(entities);
	}

	@Override
	public Predicate predicate(EntityQueryContext ctx) {
		
		TableReference lref = ctx.getTableRef(left);

		M meta = left.getMetaData();		
		BaseTable t = meta.getBaseTable();
				
		ForeignKey fk = meta.getForeignKey(key.name());
		
		// RowConstructor c = new RowConstructor(values);
		
		// new In(left, values)
		
//		EntityQueryValue
		
//		fk.getReferenced(referencingColumn)
//		fk.getReferencedColumnName(referencingColumn);
		
		
//		cm.
		
		
//		ValueExpression a = left.expression(ctx);
//		ValueExpression b = right.expression(ctx);		
//		return op.newComparison(a, b);
		
		return null;
	}
	
}