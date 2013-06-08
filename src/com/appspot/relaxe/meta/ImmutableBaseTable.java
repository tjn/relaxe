/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import java.util.Map;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaElementName;

public class ImmutableBaseTable
	extends AbstractImmutableBaseTable	
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2458408040261055930L;
		
	private PrimaryKey primaryKey;	
	private SchemaElementMap<ForeignKey> foreignKeyMap;
	private ColumnMap columnMap;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected ImmutableBaseTable() {
	}
		
	private ImmutableBaseTable(Environment environment, SchemaElementName tableName, ColumnMap columnMap) {
		super(environment, tableName);
		this.columnMap = columnMap;
	}
	
	
	private static class ForeignKeyMap
		extends AbstractSchemaElementMap<ForeignKey> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -4344734322943134268L;
		
		/**
		 * 			 * No-argument constructor for GWT Serialization
		 */
		private ForeignKeyMap() {		
		}
		
		private ForeignKeyMap(Environment environment, Map<Identifier, ForeignKey> content) {
			super(environment, content);
		}
	}
	

	public static class Builder
		extends ImmutableTable.Builder<BaseTable> {
		
		private ImmutablePrimaryKey.Builder primaryKeyBuilder;		
		private Map<Identifier, ImmutableForeignKey.Builder> foreignKeyBuilderMap;
		private ImmutableBaseTable prepared;
		private ImmutableBaseTable finished;
								
		public Builder(Environment environment, SchemaElementName sen) {
			super(environment, sen);			
		}
		
		private Map<Identifier, ImmutableForeignKey.Builder> getForeignKeyBuilderMap() {
			if (foreignKeyBuilderMap == null) {
				foreignKeyBuilderMap = createMap();				
			}

			return foreignKeyBuilderMap;
		}
		
		public ImmutableForeignKey.Builder getForeignKeyBuilder(Identifier constraintName) {
			if (constraintName == null) {
				return null;
			}
			
			Map<Identifier, ImmutableForeignKey.Builder> fkbm = getForeignKeyBuilderMap();			
			ImmutableForeignKey.Builder fkb = fkbm.get(constraintName);			
			return fkb;			
		}
		
		protected ImmutableBaseTable getPrepared() {
			return prepared;
		}
				
		public ImmutableForeignKey.Builder createForeignKeyBuilder(Identifier constraintName, SchemaElementName referenced, SchemaElementNameMap<ImmutableBaseTable.Builder> tbmap) {
			if (constraintName == null) {
				throw new NullPointerException("constraintName");
			}
						
			Map<Identifier, ImmutableForeignKey.Builder> fkbm = getForeignKeyBuilderMap();			
			ImmutableBaseTable.Builder rb = tbmap.get(referenced);
			ImmutableBaseTable pref = rb.getPrepared();
			
			ImmutableForeignKey.Builder fkb = new ImmutableForeignKey.Builder(constraintName, getPrepared(), pref);
			fkbm.put(constraintName, fkb);
			
			return fkb;			
		}
				
		public ImmutablePrimaryKey.Builder createPrimaryKeyBuilder(BaseTable table) {
			this.primaryKeyBuilder = new ImmutablePrimaryKey.Builder(table);
			
			return primaryKeyBuilder;				
		}		
		
		public ImmutablePrimaryKey.Builder getPrimaryKeyBuilder() {
			return primaryKeyBuilder;
		}
		
		public boolean prepare() {
			boolean ret = (this.prepared == null);
			
			if (this.prepared == null) {
				Environment env = getEnvironment();			
				ColumnMap cm = getColumnMapBuilder().newColumnMap();
							
				ImmutableBaseTable table = new ImmutableBaseTable(env, this.getName(), cm);									
				this.primaryKeyBuilder = createPrimaryKeyBuilder(table);
				this.prepared = table;
			}
			
			return ret; 
		}
		
		public ImmutableBaseTable getResult() {
			if (this.finished != null) {
				return this.finished;
			}		
						
			ImmutablePrimaryKey.Builder pkb = getPrimaryKeyBuilder();
			
			if (pkb != null) {
				ImmutablePrimaryKey pk = pkb.newConstraint();
				prepared.setPrimaryKey(pk);
			}			
						
			Environment env = getEnvironment();
			Map<Identifier, ImmutableForeignKey.Builder> bm = this.foreignKeyBuilderMap;						
			
			if (bm == null || bm.isEmpty()) {
				prepared.setForeignKeyMap(new EmptyForeignKeyMap(env));
			}
			else {
				Map<Identifier, ForeignKey> fkmap = createMap();
				
				for (ImmutableForeignKey.Builder b : foreignKeyBuilderMap.values()) {
					ImmutableForeignKey fk = b.newConstraint();
					fkmap.put(fk.getUnqualifiedName(), fk);
				}
				
				prepared.setForeignKeyMap(new ForeignKeyMap(env, fkmap));
			}
			
			finished = prepared;
			
			return finished;		
		}
	}

	@Override
	public SchemaElementMap<ForeignKey> foreignKeys() {
		return this.foreignKeyMap;
	}

	@Override
	public PrimaryKey getPrimaryKey() {
		return this.primaryKey;
	}
	
	private void setPrimaryKey(PrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	private void setForeignKeyMap(SchemaElementMap<ForeignKey> foreignKeyMap) {
		this.foreignKeyMap = foreignKeyMap;
	}
	
	@Override
	public ColumnMap columnMap() {
		return this.columnMap;
	}
}
