/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.appspot.relaxe.expr.Identifier;

public class ImmutableColumnMap
	implements ColumnMap {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -7272650712740797425L;
	private Environment environment;
	private Map<Identifier, Column> columnMap;
	private Column[] columnList;
	private transient Set<Identifier> keySet;
	private transient Collection<Column> values;
		
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private ImmutableColumnMap() {
	}	
	
	private ImmutableColumnMap(Environment environment, Map<Identifier, Column> columnMap, Column[] columnList) {
		this();
		this.environment = environment;
		this.columnMap = columnMap;
		this.columnList = columnList;		
	}

	public static class Builder
		extends MetaObjectBuilder {
		
		private Map<Identifier, Column> content;
		private List<Column> columnList = new ArrayList<Column>();

		public Builder(Environment environment) {
			super(environment);
		}
		
		public int getColumnCount() {
			return columnList.size();
		}		
		
		
		public Set<Identifier> names() {
			if (content == null) {
				return Collections.emptySet();
			}
			
			return Collections.unmodifiableSet(this.content.keySet());
		}
		
		public void add(Column c) {
			Identifier name = validate(c.getColumnName());			
			add(name, c);
		}		
				
		public void add(Identifier name, int ordinalPosition, DataType type, Boolean autoIncrement, String remarks, boolean definitelyNotNullable, String columnDefault) {
			name = validate(name);				
			add(name, new ImmutableColumn(name, ordinalPosition, type, autoIncrement, remarks, definitelyNotNullable, columnDefault));		
		}
		
		private void add(Identifier name, Column column) {
			getContent().put(name, column);
			columnList.add(column);
		}
				
		public Column getColumn(Identifier name) {
			return (this.content == null) ? null : this.content.get(name);
		}
		
		private Identifier validate(Identifier name) {
			if (name == null) {
				throw new NullPointerException("name");
			}
			
			Map<Identifier, Column> m = getContent();
			
			if (m.containsKey(name)) {
				throw new IllegalArgumentException("duplicate column name: '" + name.getName());
			}
			
			return name;
		}

		private Map<Identifier, Column> getContent() {
			if (content == null) {
				content = createMap();				
			}

			return content;
		}
				
		public ColumnMap newColumnMap() {			
			Column[] ca = new Column[columnList.size()];			
			ca = columnList.toArray(ca);			
			ImmutableColumnMap im = new ImmutableColumnMap(getEnvironment(), getContent(), ca);			
//			reset();
			return im;						
		}		
		
		public void reset() {
			this.content = null;
			this.columnList.clear();			
		}
	}
	

	@Override
	public Column get(int index) {	
		return columnList[index];
	}

	@Override
	public Column get(Identifier name) {
		return this.columnMap.get(name);
	}

	@Override
	public Column get(String name) {
		if (name == null) {
			return null;
		}		
		
		Identifier ci = getEnvironment().getIdentifierRules().toIdentifier(name);				
		return get(ci);
	}

	@Override
	public Environment getEnvironment() {
		return this.environment;
	}

	@Override
	public Set<Identifier> keySet() {
		if (keySet == null) {
			keySet = Collections.unmodifiableSet(this.columnMap.keySet()); 			
		}

		return keySet;
	}

	@Override
	public int size() {
		return this.columnList.length;
	}

	@Override
	public Collection<Column> values() {
		if (values == null) {			
			values = Collections.unmodifiableList(Arrays.asList(this.columnList)); 			
		}

		return values;		
	}
	
	@Override
	public boolean contains(Identifier name) {
		return (name == null) ? false : this.columnMap.containsKey(name);
	}

	@Override
	public boolean isEmpty() {
		return (this.columnList == null) || (this.columnList.length == 0);
	}
}
