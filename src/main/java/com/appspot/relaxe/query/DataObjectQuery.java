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
package com.appspot.relaxe.query;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.appspot.relaxe.expr.ColumnReference;
import com.appspot.relaxe.expr.DefaultTableExpression;
import com.appspot.relaxe.expr.ElementList;
import com.appspot.relaxe.expr.From;
import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.expr.Select;
import com.appspot.relaxe.expr.SelectListElement;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.Table;


public class DataObjectQuery {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8519860791435742037L;
	
	private transient DefaultTableExpression queryExpression;
	private Table table;
		
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected DataObjectQuery() {
	}
	
	public DataObjectQuery(Table table) {	
		this.table = table;
	}
	
	public QueryExpression getQueryExpression() throws QueryException {
		if (queryExpression == null) {
			TableReference tr = new TableReference(table);
			From f = new From(tr);		
			Collection<Column> cols = table.columnMap().values();
			List<SelectListElement> el = new ArrayList<SelectListElement>(cols.size());
			
			for (Column c : cols) {
				el.add(new ColumnReference(tr, c));
			}
			
			ElementList<SelectListElement> sel = new ElementList<SelectListElement>(el);			
			Select s = new Select(sel);
			DefaultTableExpression e = new DefaultTableExpression(s, f);
			
			this.queryExpression = e;
		}
		
		return this.queryExpression;
	}	
}
