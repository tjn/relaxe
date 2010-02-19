/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Environment;
import fi.tnie.db.meta.SchemaMap;

public class DefaultMutableCatalog
	implements Catalog {
	
	private Identifier name;	
//	private Comparator<Identifier> identifierComp;
	private Environment environment;

	private DefaultSchemaMap schemaMap;
	
	public DefaultMutableCatalog(Environment environment) {
		super();
		this.environment = environment;
	}
	
	public DefaultMutableCatalog(Environment environment, String name) {
		this(environment, environment.createIdentifier(name));
	}

	public DefaultMutableCatalog(Environment environment, Identifier name) {
		super();
		
		if (environment == null) {
			throw new NullPointerException("'environment' must not be null");
		}
		
		this.environment = environment;
		
		if (name == null) {
			throw new NullPointerException("'name' must not be null");
		}		
		
		this.name = name;
	}

//	@Override
//	public Map<String, Schema> schemas() {
//		if (nodeManager.nodes().isEmpty()) {
//			return Collections.emptyMap();
//		}
//		
//		return new LinkedHashMap<String, Schema>(nodeManager.nodes());
//	}
			
	public boolean addSchema(DefaultMutableSchema newSchema) {		
		return getSchemaMap().add(newSchema);		
	}



	private DefaultSchemaMap getSchemaMap() {
		if (schemaMap == null) {
			schemaMap = new DefaultSchemaMap(this);			
		}

		return schemaMap;
	}	
	
//	protected <E> TreeMap<Identifier, E> createIdentifierMap() {
//		return new TreeMap<Identifier, E>(identifierComparator());	
//	}

	@Override
	public Identifier getUnqualifiedName() {		
		return this.name;
	}

	@Override
	public SchemaMap schemas() {
		return getSchemaMap();
	}

	private static class DefaultSchemaMap
		extends DefaultElementMap<DefaultMutableSchema> 
		implements SchemaMap {

		public DefaultSchemaMap(Catalog catalog) {
			super(catalog);
		}		
	}

	@Override
	public Identifier getName() {
		return this.name;
	}

	@Override
	public Environment getEnvironment() {
		return this.environment;
	}
	
	
	
}
