/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.query;


import java.util.Collection;

import fi.tnie.db.ent.QueryExpressionSource;
import fi.tnie.db.expr.ColumnReference;
import fi.tnie.db.expr.DefaultTableExpression;
import fi.tnie.db.expr.From;
import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.expr.Select;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Table;

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
			
			Collection<? extends Column> cols = table.columns();
			
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
