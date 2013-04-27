/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.query;


import java.util.Collection;

import com.appspot.relaxe.ent.QueryExpressionSource;
import com.appspot.relaxe.expr.ColumnReference;
import com.appspot.relaxe.expr.DefaultTableExpression;
import com.appspot.relaxe.expr.From;
import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.expr.Select;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.Table;


public class DataObjectQuery
	implements QueryExpressionSource {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8519860791435742037L;
	
	private transient DefaultTableExpression tableExpression;
	private Table table;
		
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected DataObjectQuery() {
	}
	
	public DataObjectQuery(Table table) {	
		this.table = table;
	}

	public QueryExpression getTableExpression() {
		if (tableExpression == null) {
			TableReference tr = new TableReference(table);
			From f = new From(tr);		
			Select s = new Select();
			
			Collection<Column> cols = table.columnMap().values();
			
			for (Column c : cols) {
				s.add(new ColumnReference(tr, c));
			}
					
			DefaultTableExpression e = new DefaultTableExpression(s, f);
			
			this.tableExpression = e;
		}
		
		return this.tableExpression;
	}

	@Override
	public QueryExpression getQueryExpression() throws QueryException {
		return getTableExpression();
	}	
}
