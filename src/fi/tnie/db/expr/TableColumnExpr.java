/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import org.apache.log4j.Logger;

import fi.tnie.db.meta.Column;

public class TableColumnExpr
	extends ColumnExpr 
	implements ValueExpression {

	private static Logger logger = Logger.getLogger(TableColumnExpr.class);
	
//	private Column column;
	private TableColumnName columnName;
		
	public TableColumnExpr(AbstractTableReference table, Column column) {
		super(table, new TableColumnName(column));
		
		if (column == null) {
			throw new NullPointerException("'column' must not be null");
		}
		
		this.columnName = (TableColumnName) getColumnName();
	}
	
//	public TableColumnExpr(TableReference r, String name) {
//		super(r);
//		
//		Table t = r.getTable();				
//		Column c = t.columns().get(name);
//		
//		if (c == null) {
//			throw new IllegalArgumentException("no column '" + name + "' " +
//					"in table: " + t.getQualifiedName());			
//		}
//				
//		this.columnName = new TableColumnName(c);
//	}

	@Override
	public int getType() {				
		return getColumn().getDataType().getDataType();
	}

//	@Override
//	public String getName() {
//		return getColumn().getName();
//	}
	
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		AbstractTableReference tref = getTable();
						
		if (tref != null) {
			Identifier cn = tref.getCorrelationName(v.getContext());
			
			if (cn != null) {			
				logger().debug("corr. name: " + cn);			
				cn.traverse(vc, v);
				Symbol.DOT.traverse(vc, v);
			}
		}
		
		this.columnName.traverse(vc, v);
//		new TableColumnName(this.column).traverse(vc, v);
	}
	
	
	public Column getColumn() {		
		return columnName.getColumn();
	}
	
	public static Logger logger() {
		return TableColumnExpr.logger;
	}
}
