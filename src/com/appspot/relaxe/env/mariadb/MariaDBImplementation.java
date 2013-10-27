/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.mariadb;

import java.util.Properties;

import com.appspot.relaxe.env.CatalogFactory;
import com.appspot.relaxe.env.DefaultImplementation;
import com.appspot.relaxe.expr.DefaultSQLSyntax;
import com.appspot.relaxe.expr.DeleteStatement;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.SQLSyntax;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.expr.ddl.AlterTableDropConstraint;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.PrimaryKey;
import com.appspot.relaxe.meta.impl.mariadb.MariaDBEnvironment;
import com.appspot.relaxe.meta.impl.mysql.MySQLAlterTableDropForeignKey;
import com.appspot.relaxe.meta.impl.mysql.MySQLAlterTableDropPrimaryKey;
import com.appspot.relaxe.meta.impl.mysql.MySQLDeleteStatement;

/**
 * @author Administrator
 *
 */
public class MariaDBImplementation
	extends DefaultImplementation<MariaDBImplementation> {

	private MariaDBSyntax syntax;
    
	@Override
	public CatalogFactory catalogFactory() {
		return new MariaDBCatalogFactory(environment());
	}

    @Override
    public String defaultDriverClassName() {
        return "org.mariadb.jdbc.Driver";
    }

    @Override
    public SQLSyntax getSyntax() {
        if (syntax == null) {
            syntax = new MariaDBSyntax();
        }

        return syntax;
    }

    public static class MariaDBSyntax
        extends DefaultSQLSyntax {

        @Override
        public DeleteStatement newDeleteStatement(TableReference tref, Predicate p) {
            return new MySQLDeleteStatement(tref, p);
        }
                
        @Override
        public AlterTableDropConstraint newAlterTableDropForeignKey(ForeignKey fk) {
        	return new MySQLAlterTableDropForeignKey(fk);
        }
        
        @Override
        public AlterTableDropConstraint newAlterTableDropPrimaryKey(PrimaryKey pk) {
        	return new MySQLAlterTableDropPrimaryKey(pk);
        }
    }

    @Override
	public String createJdbcUrl(String database) {
    	return createJdbcUrl(null, database);
    }

	@Override
	public String createJdbcUrl(String host, String database) {
		host = (host == null) ? "" : host;
		return "jdbc:mariadb://" + host + "/" + database;

	}

	@Override
	public String createJdbcUrl(String host, int port, String database) {
		host = (host == null) ? "" : host;
		return "jdbc:mariadb://" + host + ":" + port + "/" + database;
	}
	
	@Override
	public MariaDBEnvironment environment() {
		return MariaDBEnvironment.environment();
	}
	
	@Override
	public MariaDBImplementation self() {
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
