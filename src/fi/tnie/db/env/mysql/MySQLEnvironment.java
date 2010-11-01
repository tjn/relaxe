/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.mysql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import fi.tnie.db.AttributeExtractor;
import fi.tnie.db.ValueExtractor;
import fi.tnie.db.ValueExtractorFactory;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Identifiable;
import fi.tnie.db.env.CatalogFactory;
import fi.tnie.db.env.DefaultImplementation;
import fi.tnie.db.env.GeneratedKeyHandler;
import fi.tnie.db.expr.DefaultSQLSyntax;
import fi.tnie.db.expr.DeleteStatement;
import fi.tnie.db.expr.InsertStatement;
import fi.tnie.db.expr.MySQLDeleteStatement;
import fi.tnie.db.expr.Predicate;
import fi.tnie.db.expr.SQLSyntax;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.expr.ddl.ColumnDefinition;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.types.ReferenceType;

/**
 * @author Administrator
 *
 */
public class MySQLEnvironment
	extends DefaultImplementation {
    
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

	@Override
	public GeneratedKeyHandler generatedKeyHandler() {				
		return new MySQLGeneratedKeyHandler();
	}

    private final class MySQLGeneratedKeyHandler implements GeneratedKeyHandler {
		@Override
		public <
			A extends Enum<A> & Identifiable, 
			R extends Enum<R> & Identifiable, 
			Q extends Enum<Q> & Identifiable, 
			T extends ReferenceType<T>,
			E extends Entity<A, R, Q, T, ? extends E>
		> 
		void processGeneratedKeys(
			InsertStatement ins, E target, ResultSet rs) 
			throws EntityException, SQLException {
			ResultSetMetaData meta = rs.getMetaData();
			
			EntityMetaData<A, R, Q, T, ? extends E> em = target.getMetaData();						
			ValueExtractorFactory vef = getValueExtractorFactory();
			
//			ResultSet is expected to contain single column: GENERATED_KEY
			ValueExtractor<?, ?> ve = vef.createExtractor(meta, 1);
			
			// MySQL supports max one auto-increment column per table:						
			Column col = findAutoIncrementColumn(em.getBaseTable());
			
			if (col == null) {
				throw new EntityException(
						"unable to find AUTO_INCREMENT column from table " + 
						em.getBaseTable());				
			}
			
			A a = em.getAttribute(col);			
			AttributeExtractor<A, R, Q, T, E> ae = new AttributeExtractor<A, R, Q, T, E>(a, ve);			
			ae.extract(rs, target);			
		}

		private Column findAutoIncrementColumn(BaseTable tbl) {						
			for (Column col : tbl.columnMap().values()) {
				if (Boolean.TRUE.equals(col.isAutoIncrement())) {
					return col;					
				}
			}
			
			return null;
		}
	}

}