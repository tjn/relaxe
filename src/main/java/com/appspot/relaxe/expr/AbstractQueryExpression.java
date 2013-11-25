/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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
package com.appspot.relaxe.expr;

import com.appspot.relaxe.ent.QueryExpressionSource;

/**
 * Top-level SELECT -statement
 * @author Administrator
 */

public abstract class AbstractQueryExpression
	extends CompoundElement
	implements QueryExpressionSource, QueryExpression {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5828745586955669361L;
	
	protected AbstractQueryExpression() {
		super();
	}	
	
	/* (non-Javadoc)
	 * @see com.appspot.relaxe.expr.QueryExpression#getTableExpr()
	 */
	@Override
	public abstract TableExpression getTableExpr();
	/* (non-Javadoc)
	 * @see com.appspot.relaxe.expr.QueryExpression#getOrderBy()
	 */
	@Override
	public abstract OrderBy getOrderBy();
	/* (non-Javadoc)
	 * @see com.appspot.relaxe.expr.QueryExpression#getLimit()
	 */
	@Override
	public abstract Limit getLimit();
	/* (non-Javadoc)
	 * @see com.appspot.relaxe.expr.QueryExpression#getOffset()
	 */
	@Override
	public abstract Offset getOffset();
//	public abstract Select getSelect();
	
	@Override
	public QueryExpression getQueryExpression() {
		return this;
	}
}