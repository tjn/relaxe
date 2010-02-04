/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import fi.tnie.db.expr.ColumnExpr;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.expr.ValueExpression;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Table;

public abstract class BaseTableRowMetaData<C extends Enum<C>>
	implements RowMetaData {
	 
	private BaseTable table;	
	private TableReference source;
	
	private EnumSet<C> pkDefinition = null; 
		
	public BaseTableRowMetaData(EnumSet<C> columns, TableReference tref) {
		super();
				
		if (tref == null) {
			throw new NullPointerException("'tref' must not be null");
		}
		
		this.source = tref;
		
		Class<C> cnt = getColumnNameType();
		
		if (columns == null) {
			columns = EnumSet.allOf(cnt);
		}
		
		Table table = this.source.getTable();
		
		if (table == null) {
			throw new NullPointerException("'table' must not be null");
		}
		
		if (!table.isBaseTable()) {
			throw new IllegalArgumentException("table must be a base table: " + table.getName()); 
		}
		
		this.table = (BaseTable) table;
		
		List<? extends Column> pkcols = this.table.getPrimaryKey().columns();
		EnumSet<C> pk = EnumSet.noneOf(cnt); 
		
		for (Column c : pkcols) {
			String n = c.getColumnName().getName();
			pk.add(Enum.valueOf(cnt, n));
		}
		
		if (!columns.containsAll(pk)) {
			throw new IllegalArgumentException("columns must contain all the primary-key columns");
		}
		
		this.pkDefinition = pk;
		
//		this.columns = columns;
	}
	
	@Override
	public int getColumnCount() {
//		return this.columns.size();
		return 0;
	}

	@Override
	public ValueExpression getColumnExpr(int column) {		
		return null;
	}
	
	public ColumnExpr getColumnExpr(C column) {
		return null;
	}
	
	public abstract Class<C> getColumnNameType();

	public BaseTable getTable() {
		return table;
	}

	public Set<C> getPKDefinition() {
		return Collections.unmodifiableSet(pkDefinition);
	}	
}