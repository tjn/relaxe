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

import com.appspot.relaxe.expr.ddl.DefaultDefinition;


public class ElementVisitorAdapter implements ElementVisitor {

	private QueryContext context;
	
	@Override
	public VisitContext start(VisitContext vc, Element e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Select e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, From e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Where e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Having e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, OrderBy e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Predicate e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, SQLKeyword e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, AbstractTableReference e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, JoinType e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, ColumnReference e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Symbol e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, SchemaElementName e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Parameter<?, ?, ?> e) {
		return null;
	}

	@Override
	public void end(Element e) {
	}

	@Override
	public QueryContext getContext() {
		if (context == null) {
			context = new SimpleQueryContext();			
		}

		return context;
	}

	@Override
	public VisitContext start(VisitContext vc, GroupBy e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Token e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Assignment e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, ValueExpression e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Identifier e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Name e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, DefaultDefinition e) {
		return null;
	}
}
