/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe.meta;

import java.util.Comparator;
import java.util.Map;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.SchemaName;

public class ImmutableSchema	
	implements Schema
{	
	private Environment environment;
	private SchemaName schemaName;	
	private SchemaElementMap<BaseTable> baseTableMap;		
	private SchemaElementMap<ForeignKey> foreignKeyMap;
	private SchemaElementMap<PrimaryKey> primaryKeyMap;
	
	private SchemaElementMap<Table> tableMap;
	private SchemaElementMap<Constraint> constraintMap;

	/**
	 * 
	 */
	private static final long serialVersionUID = -2458408040261055930L;
		
//	private SchemaElementMap<ForeignKey> foreignKeyMap;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected ImmutableSchema() {
	}
		
	protected ImmutableSchema(Environment environment, SchemaName schemaName,
			SchemaElementMap<Table> tableMap,
			SchemaElementMap<BaseTable> baseTableMap,
			SchemaElementMap<Constraint> constraintMap,
			SchemaElementMap<PrimaryKey> primaryKeyMap,
			SchemaElementMap<ForeignKey> foreignKeyMap
	) {
		this.environment = environment;
		this.schemaName = schemaName;
		this.tableMap = tableMap;
		this.baseTableMap = baseTableMap;
		this.constraintMap = constraintMap;
		this.primaryKeyMap = primaryKeyMap;
		this.foreignKeyMap = foreignKeyMap;
	}
	
	
	public static class ForeignKeyMap
		extends AbstractSchemaElementMap<ForeignKey> {				
		private static final long serialVersionUID = 9178531908351998296L;

		private ForeignKeyMap() {		
		}
		
		private ForeignKeyMap(Environment environment, Map<Identifier, ForeignKey> content) {
			super(environment, content);
		}
	}
	
	public static class ConstraintMap
		extends AbstractSchemaElementMap<Constraint> {				
	
		/**
		 * 
		 */
		private static final long serialVersionUID = 5465314543360752534L;

		private ConstraintMap() {		
		}
		
		private ConstraintMap(Environment environment, Map<Identifier, Constraint> content) {
			super(environment, content);
		}
	}
	
	public static class PrimaryKeyMap
		extends AbstractSchemaElementMap<PrimaryKey> {				
		
		private static final long serialVersionUID = 2113585775686760605L;

		private PrimaryKeyMap() {		
		}
		
		private PrimaryKeyMap(Environment environment, Map<Identifier, PrimaryKey> content) {
			super(environment, content);
		}
	}
	
	public static class TableMap
		extends AbstractSchemaElementMap<Table> {				
			
		/**
		 * 
		 */
		private static final long serialVersionUID = -3600245669776360206L;
	
		private TableMap() {		
		}
		
		private TableMap(Environment environment, Map<Identifier, Table> content) {
			super(environment, content);
		}
	}
	
	public static class BaseTableMap
		extends AbstractSchemaElementMap<BaseTable> {				
				
		/**
		 * 
		 */
		private static final long serialVersionUID = -3600245669776360206L;

		private BaseTableMap() {		
		}
		
		private BaseTableMap(Environment environment, Map<Identifier, BaseTable> content) {
			super(environment, content);
		}
	}
	
	
	

	public static class Builder
		extends MetaObjectBuilder {

		private Map<Identifier, Table> tableMap;
		private Map<Identifier, BaseTable> baseTableMap;		
		private Map<Identifier, ForeignKey> foreignKeyMap;
		private Map<Identifier, PrimaryKey> primaryKeyMap;		
		private Map<Identifier, Constraint> constraintMap;
		
		private SchemaName schemaName;
				
		public SchemaName getSchemaName() {
			return schemaName;
		}
								
		public Builder(Environment environment, SchemaName schemaName) {
			super(environment);
			
			if (schemaName == null) {
				throw new NullPointerException("schemaName");
			}
			
			this.schemaName = schemaName;
		}
		
		public Map<Identifier, Constraint> getConstraintMap() {
			if (constraintMap == null) {
				constraintMap = createMap();				
			}

			return constraintMap;
		}
		
		private Map<Identifier, ForeignKey> getForeignKeyMap() {
			if (foreignKeyMap == null) {
				foreignKeyMap = createMap();				
			}

			return foreignKeyMap;
		}
		
		private Map<Identifier, PrimaryKey> getPrimaryKeyMap() {
			if (primaryKeyMap == null) {
				primaryKeyMap = createMap();				
			}

			return primaryKeyMap;
		}
		
		private Map<Identifier, BaseTable> getBaseTableMap() {
			if (baseTableMap == null) {
				baseTableMap = createMap();				
			}

			return baseTableMap;
		}
		
		private Map<Identifier, Table> getTableMap() {
			if (tableMap == null) {
				tableMap = createMap();				
			}

			return tableMap;
		}
		
		public void add(BaseTable table) {						
			addTable(table, getBaseTableMap());			
		}
		
		public void add(PrimaryKey primaryKey) {
			if (primaryKey == null) {
				throw new NullPointerException("primaryKey");
			}
			
			SchemaElementName sen = primaryKey.getName();
			
			if (sen == null) {
				throw new NullPointerException("primaryKey.getName()");
			}
			
			addConstraint(primaryKey, getPrimaryKeyMap());
		}
				
		public void add(ForeignKey foreignKey) {
			if (foreignKey == null) {
				throw new NullPointerException("foreignKey");
			}
			
			SchemaElementName sen = foreignKey.getName();
			
			if (sen == null) {
				throw new NullPointerException("foreignKey.getName()");
			}
			
			addConstraint(foreignKey, getForeignKeyMap());
		}
				
		public BaseTable getTable(Identifier unqualifiedName) {
			return getBaseTableMap().get(unqualifiedName);
		}

		private 
		<C extends Constraint>
		void addConstraint(C constraint, Map<Identifier, C> dest) {
			SchemaElementName sen = checkQualifier(constraint);
			
			Identifier key = sen.getUnqualifiedName();
			Map<Identifier, Constraint> cm = getConstraintMap();
			
			if (cm.containsKey(key)) {
				throw new IllegalArgumentException("duplicate constraint name: " + sen.generate());
			}
			
			cm.put(key, constraint);
			dest.put(key, constraint);
		}
		
		private 
		<T extends Table>
		void addTable(T table, Map<Identifier, T> dest) {
			if (table == null) {
				throw new NullPointerException("table");
			}
			
			SchemaElementName sen = checkQualifier(table);
			
			Identifier key = sen.getUnqualifiedName();
			Map<Identifier, Table> tm = getTableMap();
			
			if (tm.containsKey(key)) {
				throw new IllegalArgumentException("duplicate table name: " + sen.generate());
			}
			
			tm.put(key, table);
			dest.put(key, table);
		}		

		private SchemaElementName checkQualifier(SchemaElement se) {
			SchemaElementName sen = se.getName();
			SchemaName q = sen.getQualifier();
			
			if (!equal(this.schemaName, q)) {
				throw new IllegalArgumentException("schema element (" + q.generate() + ") does not belong to this schema (" + this.schemaName.generate() + ")");
			}
			
			return sen;
		}
		
		private boolean equal(SchemaName a, SchemaName b) {
			Comparator<Identifier> icmp = getEnvironment().getIdentifierRules().comparator();
			
			return 
				(icmp.compare(a.getCatalogName(), b.getCatalogName()) == 0) &&
				(icmp.compare(a.getSchemaName(), b.getSchemaName()) == 0);
		}
		
		
		public ImmutableSchema newSchema() {
			Environment env = getEnvironment();
			
			ImmutableSchema schema = new ImmutableSchema(
					env, 
					schemaName,
					new TableMap(env, getTableMap()),
					new BaseTableMap(env, getBaseTableMap()),
					new ConstraintMap(env, getConstraintMap()),
					new PrimaryKeyMap(env, getPrimaryKeyMap()),
					new ForeignKeyMap(env, getForeignKeyMap())
			);
									
			return schema;
		}		
	}	
	
	@Override
	public SchemaElementMap<Table> tables() {
		return this.tableMap;
	}

	@Override
	public SchemaElementMap<ForeignKey> foreignKeys() {
		return this.foreignKeyMap;
	}

	@Override
	public SchemaElementMap<BaseTable> baseTables() {
		return this.baseTableMap;
	}

	@Override
	public SchemaElementMap<Constraint> constraints() {
		return this.constraintMap;
	}

	@Override
	public Identifier getCatalogName() {
		return this.schemaName.getCatalogName();
	}

	@Override
	public Environment getEnvironment() {
		return this.environment;
	}

	@Override
	public Identifier getUnqualifiedName() {
		return this.schemaName.getSchemaName();
	}

	@Override
	public SchemaElementMap<PrimaryKey> primaryKeys() {
		return this.primaryKeyMap;
	}

	@Override
	public SchemaName getName() {
		return this.schemaName;
	}	
}
