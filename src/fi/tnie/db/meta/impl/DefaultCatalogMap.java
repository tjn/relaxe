/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.io.Serializable;

import fi.tnie.db.meta.CatalogMap;
import fi.tnie.db.meta.SerializableEnvironment;

public class DefaultCatalogMap
	extends DefaultElementMap<DefaultMutableCatalog> 
	implements CatalogMap, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5643148376983569446L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected DefaultCatalogMap() {
	}

	public DefaultCatalogMap(SerializableEnvironment environment) {
		super(environment);
	}	
}
