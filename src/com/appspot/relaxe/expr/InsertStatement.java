/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import java.util.Collection;
import java.util.Collections;

import com.appspot.relaxe.meta.Table;

public class InsertStatement
	extends SQLDataChangeStatement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1598811041382537945L;

	private Table target;
	
	private ElementList<ValueRow> values;	
	private ElementList<Identifier> columnNameList;
	
	private SchemaElementName tableName;
	
	public int getTargetColumnCount() {
		if (columnNameList == null) {
			return getTarget().columnMap().size();
		}
		
		return columnNameList.getContent().size();
	}
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected InsertStatement() {
	}
	
//	public InsertStatement(Table target, ElementList<Identifier> columnNameList) {
//	    this(target, columnNameList, null);	    
//	}

	protected InsertStatement(Table target) {
		super(Name.INSERT);
		
		if (target == null) {
			throw new NullPointerException("'target' must not be null");
		}
		
		this.target = target;
	}

	
	/**
	 * Constructs a new  
	 * 
	 * @param target Target table to insert rows into.
	 * @param columnNameList Must not be null.
	 * @param valueRow May me null.
	 */
	public InsertStatement(Table target, ElementList<Identifier> columnNameList, ValueRow valueRow) {
		this(target);
		
		if (columnNameList == null) {
			throw new NullPointerException("'columnNameList' must not be null");
		}
		
		this.target = target;
		this.columnNameList = columnNameList;		
		this.values = new ElementList<ValueRow>(Collections.singletonList(valueRow));	
		
	}
	
	
	public InsertStatement(Table target, ElementList<Identifier> columnNameList, Collection<ValueRow> rows) {
		this(target);
		
		if (columnNameList == null) {
			throw new NullPointerException("'columnNameList' must not be null");
		}
		
		if (rows == null) {
			throw new NullPointerException("rows");
		}		
		
		this.target = target;
		this.columnNameList = columnNameList;		
		this.values = new ElementList<ValueRow>(rows);	
		
	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {		
		SQLKeyword.INSERT.traverse(vc, v);
		SQLKeyword.INTO.traverse(vc, v);
			
		getTableName().traverse(vc, v);
		
		ElementList<Identifier> cl = getColumnNameList();
		
		if (cl != null) {
			Symbol.PAREN_LEFT.traverse(vc, v);	
			cl.traverse(vc, v);
			Symbol.PAREN_RIGHT.traverse(vc, v);
		}
		
		SQLKeyword.VALUES.traverse(vc, v);		
		getValues().traverse(vc, v);
	}

	public Table getTarget() {
		return target;
	}

	public ElementList<ValueRow> getValues() {
		if (values == null) {
			values = new ElementList<ValueRow>();					
		}

		return values;
	}
	
//	void add(ValueRow r) {
//		getValues().add(r);
//	}
		
	protected SchemaElementName getTableName() {
		if (tableName == null) {
			SchemaElementName sen = this.target.getName();			
//			tableName = new SchemaElementName(this.target);
			tableName = sen;
		}

		return tableName;
	}
	
	protected ElementList<Identifier> getColumnNameList() {
		return columnNameList;
	}
	
	
}
