/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.meta.Table;

public class InsertStatement
	extends Statement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1598811041382537945L;

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
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected InsertStatement() {
	}
	
	public InsertStatement(Table target, ElementList<ColumnName> columnNameList) {
	    this(target, columnNameList, null);	    
	}
			
	
	/**
	 * Constructs a new  
	 * 
	 * @param target Target table to insert rows into.
	 * @param columnNameList Must not be null.
	 * @param valueRow May me null.
	 */
	public InsertStatement(Table target, ElementList<ColumnName> columnNameList, ValueRow valueRow) {
		super(Name.INSERT);
		
		if (target == null) {
			throw new NullPointerException("'target' must not be null");
		}
		
		if (columnNameList == null) {
			throw new NullPointerException("'columnNameList' must not be null");
		}
		
		this.target = target;
		this.columnNameList = new ElementList<ColumnName>();
		columnNameList.copyTo(this.columnNameList);
		
		if (valueRow != null) {
			getValues().add(valueRow);			
		}
	}
	
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

//	private void setTarget(Table target) {
//		this.target = target;
//	}
	
	public ElementList<ValueRow> getValues() {
		if (values == null) {
			values = new ElementList<ValueRow>();					
		}

		return values;
	}
	
	void add(ValueRow r) {
		getValues().add(r);
	}
		
	private SchemaElementName getTableName() {
		if (tableName == null) {
			tableName = new SchemaElementName(this.target);
		}

		return tableName;
	}
}
