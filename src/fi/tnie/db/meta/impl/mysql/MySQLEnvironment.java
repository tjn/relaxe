/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.mysql;

import fi.tnie.db.expr.DefaultSQLSyntax;
import fi.tnie.db.expr.DeleteStatement;
import fi.tnie.db.expr.MySQLDeleteStatement;
import fi.tnie.db.expr.Predicate;
import fi.tnie.db.expr.SQLSyntax;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.expr.ddl.ColumnDefinition;
import fi.tnie.db.meta.CatalogFactory;
import fi.tnie.db.meta.impl.DefaultEnvironment;

/**
 * @author Administrator
 *
 */
public class MySQLEnvironment
	extends DefaultEnvironment {
    
    private MySQLSyntax syntax;

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
        public DeleteStatement newDeleteStatement(TableReference tref,
                Predicate p) {         
            return new MySQLDeleteStatement(tref, p);
        }
    }
}
