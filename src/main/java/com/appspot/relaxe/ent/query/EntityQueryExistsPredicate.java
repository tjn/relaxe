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

import com.appspot.relaxe.EntityQueryExpressionBuilder;
import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQuery;
import com.appspot.relaxe.ent.EntityQueryContext;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.expr.op.Exists;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;

public class EntityQueryExistsPredicate
	implements EntityQueryPredicate {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9214275181479579908L;
	
	private EntityQuery<?, ?, ?, ?, ?, ?, ?, ?> inner;
	
	protected EntityQueryExistsPredicate() {
	}
			
	public EntityQueryExistsPredicate(EntityQuery<?, ?, ?, ?, ?, ?, ?, ?> inner) {
		super();		
		this.inner = inner;
	}

	@Override
	public Predicate predicate(EntityQueryContext ctx) {		
		QueryExpression qe = eval(ctx, this.inner);
		return new Exists(qe);	
	}

	private 
	<
		A extends AttributeName,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, H, F, M>,
		E extends Entity<A, R, T, E, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, H, M, F>,
		M extends EntityMetaData<A, R, T, E, H, F, M>,
		RE extends EntityQueryElement<A, R, T, E, H, F, M, RE>
	>	
	QueryExpression eval(EntityQueryContext ctx, EntityQuery<A, R, T, E, H, F, M, RE> query) {
		EntityQueryExpressionBuilder<A, R, T, E, H, F, M, RE> qb = 
				new EntityQueryExpressionBuilder<A, R, T, E, H, F, M, RE>(ctx, query);		
		QueryExpression qe = qb.getQueryExpression();		
		return qe;
	}
	
}