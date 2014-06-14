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
package com.appspot.relaxe.expr;

import java.util.Collection;

public class GroupBy extends AbstractClause {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3190593291892514712L;

	private ElementList<ValueExpression> groupingExprList;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private GroupBy() {
	}
	
	public GroupBy(ElementList<ValueExpression> expressionList) {
		super();
		
		if (expressionList == null) {
			throw new NullPointerException("expressionList");
		}
		
		this.groupingExprList = expressionList;
	}
	
	public GroupBy(Collection<ValueExpression> expressionList) {
		this(ElementList.newElementList(expressionList));
	}
		
	
//	@Override
//	public void generate(SimpleQueryContext qc, StringBuffer dest) {
//		ElementList<ValueExpression> el = getGroupingExprList();
//		
//		if (!el.isEmpty()) {
//			dest.append("GROUP BY ");
//			el.generate(qc, dest);
//		}		
//	}

	public ElementList<ValueExpression> getGroupingExprList() {
		return groupingExprList;
	}
	
	@Override
	protected Element getContent() {		
		return getGroupingExprList();
	}
	
	@Override
	protected void traverseClause(VisitContext vc, ElementVisitor v) {
		SQLKeyword.GROUP.traverse(vc, v);
		SQLKeyword.BY.traverse(vc, v);	
	}
}
