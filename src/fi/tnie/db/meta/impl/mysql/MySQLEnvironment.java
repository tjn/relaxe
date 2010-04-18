/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.mysql;

import fi.tnie.db.expr.ddl.ColumnDefinition;
import fi.tnie.db.meta.CatalogFactory;
import fi.tnie.db.meta.impl.DefaultEnvironment;

/**
 * @author Administrator
 *
 */
public class MySQLEnvironment
	extends DefaultEnvironment {

	@Override
	public CatalogFactory catalogFactory() {
		return new MySQLCatalogFactory(this);
	}

    @Override
    public ColumnDefinition serialColumnDefinition(String columnName,
            boolean big) {
        // TODO add support (subclass ColumnDefinition to put AUTO_INCREMENT in the right spot) 
        return null;
    }

    @Override
    public String driverClassName() {
        return "com.mysql.jdbc.Driver";
    }

}
