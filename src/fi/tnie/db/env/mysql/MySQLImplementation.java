/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.AttributeExtractor;
import fi.tnie.db.AttributeExtractorFactory;
import fi.tnie.db.DefaultAttributeExtractorFactory;
import fi.tnie.db.ValueExtractorFactory;
import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.ent.EntityMetaData;
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
public class MySQLImplementation
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
			A extends Attribute,
			R,
			T extends ReferenceType<T>,
			E extends Entity<A, R, T, E>
		>
		void processGeneratedKeys(
			InsertStatement ins, E target, ResultSet rs)
			throws EntityException, SQLException {

			EntityMetaData<A, R, T, E> em = target.getMetaData();
			ValueExtractorFactory vef = getValueExtractorFactory();

//			ResultSet is expected to contain single column: GENERATED_KEY

			// MySQL supports max one auto-increment column per table:
			Column col = findAutoIncrementColumn(em.getBaseTable());

			if (col == null) {
				throw new EntityException(
						"unable to find AUTO_INCREMENT column from table " +
						em.getBaseTable());
			}

			A a = em.getAttribute(col);

			AttributeExtractorFactory aef = new DefaultAttributeExtractorFactory();
			AttributeExtractor<?, ?, ?, A, E, ?> ae = aef.createExtractor(a, em, col.getDataType().getDataType(), 1, vef);

			if (rs.next()) {
				ae.extract(rs, target);
			}
			else {
				String cn = em.getBaseTable().getQualifiedName() + "." + col.getUnqualifiedName();
				throw new EntityException("can not get auto-increment key (" + cn + ")");
			}
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
