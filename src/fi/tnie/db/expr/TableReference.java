/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;
import java.util.List;

import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Table;

public class TableReference 
	extends NonJoinedTable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3388366557843780574L;
	private Table table;	
	private ElementList<ColumnName> columnNameList;
	
	private SchemaElementName tableName;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected TableReference() {
	}
		
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
		getCorrelationClause().traverse(vc, v);
	}
	
	public Table getTable() {
		return table;
	}

	@Override
	public ElementList<? extends ColumnName> getUncorrelatedColumnNameList() {
		if (columnNameList == null) {
			this.columnNameList = new ElementList<ColumnName>();
			
			List<ColumnName> nl = this.columnNameList.getContent();
			
			for (final Column c : getTable().columns()) {
				nl.add(c.getColumnName());
			}
		}
		
		return columnNameList;
	}
		
	public SchemaElementName getTableName()
		throws IllegalIdentifierException {
		if (tableName == null) {
			tableName = new SchemaElementName(getTable());			
		}

		return tableName;
	}

	@Override
	public void addAll(ElementList<SelectListElement> dest) {
		for (Column	c : getTable().columns()) {
			dest.add(new ColumnReference(this, c));
		}
	}
	
	@Override
	public int getColumnCount() {		
		return getTable().columns().size();
	}
}
