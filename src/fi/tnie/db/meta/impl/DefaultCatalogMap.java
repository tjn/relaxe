/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.io.Serializable;

import fi.tnie.db.meta.CatalogMap;
import fi.tnie.db.meta.Environment;

public class DefaultCatalogMap
	extends DefaultElementMap<DefaultMutableCatalog> 
	implements CatalogMap, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5643148376983569446L;

	public DefaultCatalogMap(Environment environment) {
		super(environment);
	}	
}
