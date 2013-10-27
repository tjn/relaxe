/*
 * Copyright (c) 2009-2013 Topi Nieminen
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
