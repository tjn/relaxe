/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.meta.Table;

public class InsertQuery
	extends CompoundElement {

	private Table target;
	
	private ElementList<ValueRow> values;	
	private ElementList<ColumnName> columnNameList;
	
	private SchemaElementName tableName;
	
	public int getTargetColumnCount() {
		if (columnNameList == null) {
			return getTarget().columns().size();
		}
		
		return columnNameList.getContent().size();
	}
			
	public InsertQuery(Table target, ElementList<ColumnName> columnNameList, ValueRow valueRow) {
		super();
		this.target = target;
				
		if (columnNameList != null) {
			this.columnNameList = new ElementList<ColumnName>();
			columnNameList.copyTo(this.columnNameList);
		}
		
		if (valueRow != null) {
			getValues().add(valueRow);			
		}
	}	
	
//	public void traverse(ElementVisitor v) {
//		traverse(null, v);
//	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {		
		Keyword.INSERT.traverse(vc, v);
		Keyword.INTO.traverse(vc, v);
			
		getTableName().traverse(vc, v);		
		
		if (columnNameList != null) {
			Symbol.PAREN_LEFT.traverse(vc, v);	
			columnNameList.traverse(vc, v);
			Symbol.PAREN_RIGHT.traverse(vc, v);
		}
		
		Keyword.VALUES.traverse(vc, v);
		getValues().traverse(vc, v);
	}

	public Table getTarget() {
		return target;
	}

	public void setTarget(Table target) {
		this.target = target;
	}
	
	public ElementList<ValueRow> getValues() {
		if (values == null) {
			values = new ElementList<ValueRow>();					
		}

		return values;
	}
	
	void add(ValueRow r) {
		getValues().add(r);
	}
	
	public String generate() {
		StringBuffer dest = new StringBuffer();
		ElementVisitor v = new QueryGenerator(dest);
		traverse(null, v);		
		return dest.toString();
	}

	private SchemaElementName getTableName() {
		if (tableName == null) {
			tableName = new SchemaElementName(this.target);
		}

		return tableName;
	}	
}
