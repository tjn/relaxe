/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import fi.tnie.db.ent.Identifiable;
import fi.tnie.db.expr.ColumnReference;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.expr.ValueExpression;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Table;

public abstract class BaseTableRowMetaData<C extends Enum<C> & Identifiable>
	implements RowMetaData {
	 
	private BaseTable baseTable;	
	private TableReference source;	
	private EnumSet<C> pkDefinition = null;
	
	private BaseTableRowFactory<C, BaseTableRow<C>, ?> factory;
	
	private List<C> columnList;
	
	private EnumMap<C, ColumnReference> exprMap;
	
	public BaseTableRowMetaData(TableReference tref, BaseTableRowFactory<C, BaseTableRow<C>, ?> factory) {
		this(null, tref, factory);		
	}
		
	public BaseTableRowMetaData(EnumSet<C> columns, TableReference tref, BaseTableRowFactory<C, BaseTableRow<C>, ?> factory) {
		super();
				
		if (tref == null) {
			throw new NullPointerException("'tref' must not be null");
		}
		
		if (factory == null) {
			throw new NullPointerException("'factory' must not be null");
		}
		
		this.source = tref;				
		this.factory = factory;
		
		Class<C> cnt = getColumnNameType();
		
		if (columns == null) {
			columns = EnumSet.allOf(cnt);
		}
		
		Table table = this.source.getTable();
		
		if (table == null) {
			throw new NullPointerException("'baseTable' must not be null");
		}
		
		if (!table.isBaseTable()) {
			throw new IllegalArgumentException("baseTable must be a base baseTable: " + table.getName()); 
		}
		
		this.baseTable = (BaseTable) table;	
								
		List<? extends Column> pkcols = this.baseTable.getPrimaryKey().columns();
		EnumSet<C> pk = EnumSet.noneOf(cnt); 
		
		for (Column c : pkcols) {
			String n = c.getColumnName().getName();
			pk.add(Enum.valueOf(cnt, n));
		}
		
		if (!columns.containsAll(pk)) {
			throw new IllegalArgumentException("columns must contain all the primary-key columns");
		}
		
		this.columnList = new ArrayList<C>(columns);				
		this.pkDefinition = pk;
	}
	
	@Override
	public int getColumnCount() {
		return this.columnList.size();
	}

	@Override
	public ValueExpression getColumnExpr(int ordinal) {
		return getTableColumnExpr(ordinal);
	}
	
	
	public ColumnReference getTableColumnExpr(int ordinal) {						
		C column = this.columnList.get(ordinal - 1);
		
		if (exprMap == null) {
			exprMap = new EnumMap<C, ColumnReference>(getColumnNameType());
		}
		
		ColumnReference e = exprMap.get(column);
		
//		if (e == null) {
//			Column c = baseTable.getColumn(column.identifier());			
//			e = new TableColumnExpr(source, c);	
//			exprMap.put(column, e);
//		}		
		
		return e;
	}
	
	public abstract Class<C> getColumnNameType();

	public BaseTable getTable() {
		return baseTable;
	}

	public Set<C> getPKDefinition() {
		return Collections.unmodifiableSet(pkDefinition);
	}

	public BaseTableRowFactory<C, BaseTableRow<C>, ?> getFactory() {
		return factory;
	}
	
}