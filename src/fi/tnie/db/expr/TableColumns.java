/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class TableColumns
	extends CompoundElement
	implements SelectListElement {

	private NonJoinedTable tableRef;

	public TableColumns(NonJoinedTable tableRef) {
		super();
		
		if (tableRef == null) {
			throw new NullPointerException("'tableRef' must not be null");
		}
		
		this.tableRef = tableRef;
	}

	@Override
	public List<? extends ColumnName> getColumnNames() {	
		return tableRef.getColumnNameList().getContent();
	}
	
	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {		
		v.start(vc, this);
		
		OrdinaryIdentifier cn = tableRef.getCorrelationName(v.getContext());				
		cn.traverse(vc, v);
		Symbol.DOT.traverse(vc, v);
		Symbol.ASTERISK.traverse(vc, v);
		
		v.end(this);
	}

	@Override
	public int getColumnCount() {
		return tableRef.getColumnNameList().getContent().size();
	}
	
	public ValueExpression getColumnExpr(int column) {
		return getTableColumnExpr(column);
	}

	@Override
	public ColumnExpr getTableColumnExpr(int column) {
		int cc = getColumnCount();
		int index = column - 1;
		
		if (index < 0 || index >= cc) {
			throw new IndexOutOfBoundsException(Integer.toString(column));
		}
			 
		return tableRef.getAllColumns().getTableColumnExpr(column);
	}		
}
