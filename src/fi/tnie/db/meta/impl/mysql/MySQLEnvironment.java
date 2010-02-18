/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.mysql;

import fi.tnie.db.meta.CatalogFactory;
import fi.tnie.db.meta.impl.DefaultEnvironment;

public class MySQLEnvironment
	extends DefaultEnvironment {

	@Override
	public CatalogFactory catalogFactory() {
		return new MySQLCatalogFactory(this);
	}

}
