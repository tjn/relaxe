/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import fi.tnie.db.meta.CatalogMap;
import fi.tnie.db.meta.Environment;

public class DefaultCatalogMap
	extends DefaultElementMap<DefaultMutableCatalog> 
	implements CatalogMap {

	public DefaultCatalogMap(Environment environment) {
		super(environment);
	}	
}
