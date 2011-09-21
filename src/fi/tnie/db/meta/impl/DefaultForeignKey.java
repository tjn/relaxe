/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;

public class DefaultForeignKey
	extends DefaultConstraint
	implements ForeignKey {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1940589015397468329L;
	private LinkedHashMap<DefaultMutableColumn, DefaultMutableColumn> columnMap;
	private Map<Column, Column> columnMapView;
		
	private DefaultMutableBaseTable referencingTable;
	private DefaultMutableBaseTable referencedTable;
	
	private int updateRule;
	private int deleteRule;
	private int deferrability;	
	
	public static class Pair {
		private DefaultMutableColumn referencing;
		private DefaultMutableColumn referenced;
		
		public Pair(DefaultMutableColumn referencing, DefaultMutableColumn referenced) {
			super();
			this.referencing = referencing;
			this.referenced = referenced;
		}		
	}
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected DefaultForeignKey() {
	}

	public DefaultForeignKey(DefaultMutableSchema schema, Identifier name, DefaultMutableColumn referencing, DefaultMutableColumn referenced) {		
		this(schema, name, Collections.singletonList(new DefaultForeignKey.Pair(referencing, referenced)));		
	}
	
	public DefaultForeignKey(DefaultMutableSchema schema, Identifier name) {
		super(schema, name);
	}

	public DefaultForeignKey(DefaultMutableSchema schema, Identifier name, List<DefaultForeignKey.Pair> mapping) {
		this(schema, name);
		setColumnMap(mapping);
	}
	
	public void setColumnMap(List<DefaultForeignKey.Pair> mapping) {
		if (mapping == null) {
			throw new NullPointerException("mapping must not be null");
		}
		
		if (mapping.isEmpty()) {
			throw new IllegalArgumentException("mapping must not be empty");
		}
		
		Map<DefaultMutableColumn, DefaultMutableColumn> cm = this.columnMap = new LinkedHashMap<DefaultMutableColumn, DefaultMutableColumn>();
						
		for (DefaultForeignKey.Pair p : mapping) {
			if (this.referencingTable == null) {
				this.referencingTable = (DefaultMutableBaseTable) p.referencing.getTable();
				this.referencingTable.add(this);
			}
			else {
				ensureSameTable(p.referencing.getTable(), this.referencingTable, 
						"all the referencing columns of the multi-column foreign key must in the same table");
			}
			
			if (this.referencedTable == null) {
				this.referencedTable = (DefaultMutableBaseTable) p.referenced.getTable();
				this.referencedTable.ref(this);
			}
			else {
				ensureSameTable(p.referenced.getTable(), this.referencedTable, 
						"all the referenced columns of the multi-column foreign key (" + getQualifiedName() + ") " +
						"must in the same table: " + this.referencedTable + "<>" + p.referenced.getTable());
			}
			
			cm.put(p.referencing, p.referenced);						
			p.referenced.add(this);
		}
		
		this.columnMapView = new LinkedHashMap<Column, Column>(cm);
	}
	
	public Map<Column, Column> columns() {
		return (columnMap == null) ? null : Collections.unmodifiableMap(this.columnMapView);						
	}
	
	
	@Override
	public BaseTable getReferenced() {
		return getReferencedTable();
	}

	@Override
	public BaseTable getReferencing() {
		return getReferencingTable();
	}

	public DefaultMutableBaseTable getReferencingTable() {
		return referencingTable;
	}

	public DefaultMutableBaseTable getReferencedTable() {
		return referencedTable;
	}
	
	private void ensureSameTable(DefaultMutableTable a, DefaultMutableTable b, String msg) {
		if (a != b) {
			throw new IllegalArgumentException(msg);
		}		
	}

	public int getUpdateRule() {
		return updateRule;
	}

	public void setUpdateRule(int updateRule) {
		this.updateRule = updateRule;
	}

	public int getDeleteRule() {
		return deleteRule;
	}

	public void setDeleteRule(int deleteRule) {
		this.deleteRule = deleteRule;
	}

	public int getDeferrability() {
		return deferrability;
	}

	public void setDeferrability(int deferrability) {
		this.deferrability = deferrability;
	}

	@Override
	public String toString() {	
		return "FK: " + getQualifiedName();
	}

	@Override
	public Type getType() {
		return Type.FOREIGN_KEY;
	}	


}
