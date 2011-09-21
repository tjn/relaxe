/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

//import org.apache.log4j.Logger;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Constraint;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.PrimaryKey;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.SchemaElementMap;
import fi.tnie.db.meta.Table;

public class DefaultMutableSchema
	implements Schema {
		
//	private static Logger logger = Logger.getLogger(DefaultMutableSchema.class);	
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 3460274773506317380L;
	
	private DefaultSchemaElementMap<DefaultMutableTable> tables;
	private DefaultSchemaElementMap<DefaultPrimaryKey> primaryKeys;
	private DefaultSchemaElementMap<DefaultForeignKey> foreignKeys;
	private DefaultSchemaElementMap<DefaultConstraint> constraintMap;
	
	private DefaultSchemaElementMap<DefaultMutableBaseTable> baseTables;
		
	private DefaultMutableCatalog catalog;	
	private Identifier name;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected DefaultMutableSchema() {
	}
		
	public DefaultMutableSchema(DefaultMutableCatalog catalog, Identifier name) {
		super();
		
		if (catalog == null) {
			throw new NullPointerException("'catalog' must not be null");
		}
		
		if (name == null) {
			throw new NullPointerException("'name' must not be null");
		}
		
		this.name = name;
		this.catalog = catalog;		
			
		catalog.addSchema(this);
	}
	
	public DefaultMutableSchema(Identifier name) {
		super();
		
		if (name == null) {
			throw new NullPointerException("'name' must not be null");
		}
		
		this.name = name;
	}
	
	public void setCatalog(DefaultMutableCatalog catalog) {
		if (this.catalog != null) {
			throw new IllegalArgumentException("catalog ahas already been set");
		}
						
		this.catalog = catalog;
		this.catalog.addSchema(this);
	}
	
	public boolean add(DefaultMutableTable newTable) {
		return getTables().add(newTable);
	}
	
	public DefaultMutableCatalog getMutableCatalog() {
		return this.catalog;
	}

	@Override
	public Catalog getCatalog() {
		return this.catalog;
	}

//	public static Logger logger() {
//		return DefaultMutableSchema.logger;
//	}
	
	@Override
	public String toString() {		
		return super.toString() + ": " + this.getUnqualifiedName();
	}

//	@Override
//	public ForeignKey getForeignKey(Identifier name) {
//		Constraint c = getConstraintMap().get(name);		
//		boolean fk = (c != null && c.getType() == Constraint.Type.FOREIGN_KEY);
//		return (ForeignKey) (fk ? c : null);
//	}
//	
//	
//
//	@Override
//	public Table getTable(Identifier name) {
//		return getTableMap().get(name);
//	}

	@Override
	public Identifier getUnqualifiedName() {		
		return this.name;
	}

//	@Override
//	public Table getTable(String name)
//		throws IllegalIdentifierException {
//		Identifier k = getCatalog().create(name);
//		return getTableMap().get(k);
//	}

//	@Override
//	public Set<Identifier> tableNames() {
//		return getTableMap().keySet();
//	}
	
//	private DefaultSchemaElementMap<DefaultMutableTable> getTableMap() {
//		if (tableMap == null) {			
//			tableMap = new DefaultSchemaElementMap<DefaultMutableTable>(catalog);
//		}
//
//		return tableMap;
//	}

	
	boolean add(DefaultPrimaryKey pk) {
		getConstraintMap().add(pk);
		return getPrimaryKeys().add(pk);
	}
	
	boolean add(DefaultForeignKey fk) {
		getConstraintMap().add(fk);
		return getForeignKeys().add(fk);
	}

	private DefaultSchemaElementMap<DefaultConstraint> getConstraintMap() {
		if (constraintMap == null) {
			constraintMap = createSchemaElementMap();
		}

		return constraintMap;
	}



	@Override
	public SchemaElementMap<? extends ForeignKey> foreignKeys() {
		return getForeignKeys();
	}

	@Override
	public SchemaElementMap<? extends PrimaryKey> primaryKeys() {
		return getPrimaryKeys();
	}

	@Override
	public SchemaElementMap<? extends Table> tables() {
		return getTables();
	}

	private DefaultSchemaElementMap<DefaultMutableTable> getTables() {
		if (tables == null) {
			tables = createSchemaElementMap();			
		}

		return tables;
	}

	private DefaultSchemaElementMap<DefaultPrimaryKey> getPrimaryKeys() {
		if (primaryKeys == null) {
			primaryKeys = createSchemaElementMap();	
		}

		return primaryKeys;
	}

	private DefaultSchemaElementMap<DefaultForeignKey> getForeignKeys() {
		if (foreignKeys == null) {
			foreignKeys = createSchemaElementMap();			
		}

		return foreignKeys;
	}

	protected <E extends DefaultSchemaElement> DefaultSchemaElementMap<E> createSchemaElementMap() {
		return new DefaultSchemaElementMap<E>(
				getCatalog().getEnvironment());
	}
	

	public SchemaElementMap<? extends BaseTable> baseTables() {		
		if (baseTables == null) {		
			baseTables = createSchemaElementMap();					
		
			for (Table t : tables().values()) {
				if (t.isBaseTable()) {
					baseTables.add((DefaultMutableBaseTable) t);
				}
			}
		}
		
		return baseTables;
	}

	@Override
	public SchemaElementMap<? extends Constraint> constraints() {
		return getConstraintMap();
	}
//	new 
//	
//	private class ConstraintMap<C extends Constraint>
//		extends UnionMap<C>
//		implements SchemaElementMap<C> {
//
//		public ConstraintMap(DefaultSchemaElementMap<? extends C> left, DefaultSchemaElementMap<? extends C> right) {
//			super(left, right);
//		}
//
//	}	
	
}
