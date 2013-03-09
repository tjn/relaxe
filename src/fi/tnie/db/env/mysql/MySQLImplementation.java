/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.mysql;

import fi.tnie.db.env.CatalogFactory;
import fi.tnie.db.env.DefaultImplementation;
import fi.tnie.db.env.GeneratedKeyHandler;
import fi.tnie.db.expr.DefaultSQLSyntax;
import fi.tnie.db.expr.DeleteStatement;
import fi.tnie.db.expr.MySQLDeleteStatement;
import fi.tnie.db.expr.Predicate;
import fi.tnie.db.expr.SQLSyntax;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.expr.ddl.ColumnDefinition;
import fi.tnie.db.meta.impl.mysql.MySQLEnvironment;

/**
 * @author Administrator
 *
 */
public class MySQLImplementation
	extends DefaultImplementation<MySQLImplementation> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6946031278704702178L;
	private MySQLSyntax syntax;
    private MySQLEnvironment environment;
    
	@Override
	public CatalogFactory catalogFactory() {
		return new MySQLCatalogFactory(this.environment());
	}

    @Override
    public ColumnDefinition serialColumnDefinition(String columnName, boolean big) {
        // TODO add support (subclass ColumnDefinition to put AUTO_INCREMENT in the right spot)
        return null;
    }

    @Override
    public String defaultDriverClassName() {
        return "com.mysql.jdbc.Driver";
    }

    @Override
    public SQLSyntax getSyntax() {
        if (syntax == null) {
            syntax = new MySQLSyntax();
        }

        return syntax;
    }

    public static class MySQLSyntax
        extends DefaultSQLSyntax {

        @Override
        public DeleteStatement newDeleteStatement(TableReference tref, Predicate p) {
            return new MySQLDeleteStatement(tref, p);
        }
    }

    public String createJdbcUrl(String database) {
    	return createJdbcUrl(null, database);
    }

	@Override
	public String createJdbcUrl(String host, String database) {
		host = (host == null) ? "" : host;
		return "jdbc:mysql://" + host + "/" + database;

	}

	@Override
	public String createJdbcUrl(String host, int port, String database) {
		host = (host == null) ? "" : host;
		return "jdbc:mysql://" + host + ":" + port + "/" + database;
	}

	@Override
	public MySQLEnvironment environment() {
		if (environment == null) {
			environment = new MySQLEnvironment();			
		}

		return environment;
	}
	
	@Override
	public MySQLImplementation self() {
		return this;
	}
}
