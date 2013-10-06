/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.mysql;

import java.util.Properties;

import com.appspot.relaxe.env.CatalogFactory;
import com.appspot.relaxe.env.DefaultImplementation;
import com.appspot.relaxe.expr.DefaultSQLSyntax;
import com.appspot.relaxe.expr.DeleteStatement;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.SQLSyntax;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.meta.impl.mysql.MySQLDeleteStatement;
import com.appspot.relaxe.meta.impl.mysql.MySQLEnvironment;

/**
 * @author Administrator
 *
 */
public class MySQLImplementation
	extends DefaultImplementation<MySQLImplementation> {

	private MySQLSyntax syntax;
    private MySQLEnvironment environment;
    
	@Override
	public CatalogFactory catalogFactory() {
		return new MySQLCatalogFactory(this.environment());
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

    @Override
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
	
	@Override
	public Properties getDefaultProperties() {
		Properties cfg = super.getDefaultProperties();
		
		cfg.setProperty("nullCatalogMeansCurrent", "false");
		cfg.setProperty("nullNamePatternMatchesAll", "false");
		cfg.setProperty("sessionVariables", "sql_mode='ANSI'");
		
		return cfg;
	}
}
