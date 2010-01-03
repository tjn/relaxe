/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;
import java.util.List;

import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Table;

public class TableReference extends AbstractTableReference {
	private Table table;
	
	private ElementList<ColumnName> columnNameList;
	private ElementList<SelectListElement> selectList;
	
	private Name tableName;
		
	public TableReference(Table table) {
		super();
		
		if (table == null) {
			throw new NullPointerException("'table' must not be null");
		}
		
		this.table = table;
	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		getTableName().traverse(vc, v);
		getCorrelationName(v.getContext()).traverse(vc, v);
	}
	
	public Table getTable() {
		return table;
	}

	@Override
	public ElementList<? extends ColumnName> getColumnNameList() {
		if (columnNameList == null) {
			this.columnNameList = new ElementList<ColumnName>();
			
			List<ColumnName> nl = this.columnNameList.getContent();
						
			for (final Column c : getTable().columns().values()) {
				nl.add(new TableColumnName(c));
			}
		}
		
		return columnNameList;
	}

	@Override
	public ElementList<SelectListElement> getSelectList() {
		// TODO: use *?
		if (selectList == null) {
			ElementList<SelectListElement> el = new ElementList<SelectListElement>();
			List<SelectListElement> cl = el.getContent();
			
			for (Column c : getTable().columns().values()) {
				cl.add(new SelectListElement(new TableColumnExpr(this, c)));
			}
			
			this.selectList = el;
		};
				
		return selectList;
	}
		
	public Name getTableName() {
		if (tableName == null) {
			tableName = new Name(getTable());			
		}

		return tableName;
	}
		
}
