/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import org.apache.log4j.Logger;

import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Table;

public class TableColumnExpr
	extends ColumnExpr implements ColumnName {

	private static Logger logger = Logger.getLogger(TableColumnExpr.class);
	
	private Column column;
		
	public TableColumnExpr(AbstractTableReference table, Column column) {
		super(table);
		
		if (column == null) {
			throw new NullPointerException("'column' must not be null");
		}
		
		this.column = column;
	}
	
	public TableColumnExpr(TableReference r, String name) {
		super(r);
		
		Table t = r.getTable();				
		Column c = t.columns().get(name);
		
		if (c == null) {
			throw new IllegalArgumentException("no column '" + name + "' " +
					"in table: " + t.getQualifiedName());			
		}
				
		this.column = c;				
	}

	@Override
	public int getType() {				
		return getColumn().getDataType().getDataType();
	}

	public Column getColumn() {
		return column;
	}

	@Override
	public String getName() {
		return getColumn().getName();
	}
	
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		AbstractTableReference tref = getTable();
		
		if (tref != null) {
			Identifier cn = tref.getCorrelationName(v.getContext());
			logger().debug("corr. name: " + cn);
			
			cn.traverse(vc, v);
			Symbol.DOT.traverse(vc, v);
		}
		
		new TableColumnName(this.column).traverse(vc, v);
	}
	
	@Override
	public String getColumnName() {		
		return getColumn().getName();
	}
	
	public static Logger logger() {
		return TableColumnExpr.logger;
	}
}
