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

import com.appspot.relaxe.ent.EntityQueryContext;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.expr.op.Comparison;

public class EntityQueryInPredicate
	implements EntityQueryPredicate {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9214275181479579908L;
	
	private Comparison.Op op;
	private EntityQueryValueReference left;	
	private EntityQueryValueReference right;
	
	public EntityQueryInPredicate(EntityQueryValueReference left,
			EntityQueryValueReference right) {
		this(Comparison.Op.EQ, left, right);
	}
	
	public EntityQueryInPredicate(Comparison.Op op, EntityQueryValueReference left,
			EntityQueryValueReference right) {
		super();
		this.op = op;
		this.left = left;
		this.right = right;
	}

	@Override
	public Predicate predicate(EntityQueryContext ctx) {
		ValueExpression a = left.expression(ctx);
		ValueExpression b = right.expression(ctx);		
		return op.newComparison(a, b);	
	}
	
}