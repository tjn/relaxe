/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.mysql;

import java.sql.ResultSetMetaData;
import fi.tnie.db.ValueExtractorFactory;
import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.ColumnResolver;
import fi.tnie.db.ent.ConstantColumnResolver;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.env.CatalogFactory;
import fi.tnie.db.env.DefaultGeneratedKeyHandler;
import fi.tnie.db.env.DefaultImplementation;
import fi.tnie.db.env.GeneratedKeyHandler;
import fi.tnie.db.expr.DefaultSQLSyntax;
import fi.tnie.db.expr.DeleteStatement;
import fi.tnie.db.expr.MySQLDeleteStatement;
import fi.tnie.db.expr.Predicate;
import fi.tnie.db.expr.SQLSyntax;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.expr.ddl.ColumnDefinition;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.impl.mysql.MySQLEnvironment;
import fi.tnie.db.types.ReferenceType;

/**
 * @author Administrator
 *
 */
public class MySQLImplementation
	extends DefaultImplementation {

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

	@Override
	public GeneratedKeyHandler generatedKeyHandler() {
		return new MySQLGeneratedKeyHandler(getValueExtractorFactory());
	}

    private final class MySQLGeneratedKeyHandler 
    	extends DefaultGeneratedKeyHandler {
    	
//		@Override
//		public
//		<
//		    A extends Attribute,
//		    R extends Reference,
//		    T extends ReferenceType<A, R, T, E, ?, ?, M, ?>,
//		    E extends Entity<A, R, T, E, ?, ?, M, ?>,
//			M extends EntityMetaData<A, R, T, E, ?, ?, M, ?>
//		>
//		void processGeneratedKeys(
//			InsertStatement ins, E target, ResultSet rs)
//			throws EntityException, SQLException {
//
//			M em = target.getMetaData();
//
////			ResultSet is expected to contain single column: GENERATED_KEY
//
//			// MySQL supports max one auto-increment column per table:
//			Column col = findAutoIncrementColumn(em.getBaseTable());
//
//			if (col == null) {
//				throw new EntityException(
//						"unable to find AUTO_INCREMENT column from table " +
//						em.getBaseTable());
//			}
//			
//									
////			if (rs.next()) {				
////				DefaultAttributeWriterFactory wf = new DefaultAttributeWriterFactory();				
////				ConstantColumnResolver cr = new ConstantColumnResolver(col);				
////				AbstractAttributeWriter<A, T, E, ?, ?, ?, ?> aw = wf.createWriter(em, cr, 1);				
////				aw.write(rs, target);
////			}
////			else {
////				String cn = em.getBaseTable().getQualifiedName() + "." + col.getUnqualifiedName();
////				throw new EntityException("can not get auto-increment key for column (" + cn + ")");
////			}
//		}
    
		public MySQLGeneratedKeyHandler(ValueExtractorFactory valueExtractorFactory) {
			super(valueExtractorFactory);
		}

		@Override
		protected 
		<
			A extends Attribute,
			R extends Reference,
			T extends ReferenceType<A, R, T, E, ?, ?, M, ?>,
			E extends Entity<A, R, T, E, ?, ?, M, ?>,
			M extends EntityMetaData<A, R, T, E, ?, ?, M, ?>
		>
		ColumnResolver createColumnResolver(ResultSetMetaData meta, M em)
			throws RuntimeException {
//			ResultSet is expected to contain single column: GENERATED_KEY
			BaseTable table = em.getBaseTable();
	
			// MySQL supports max one auto-increment column per table:
			Column col = findAutoIncrementColumn(table);
			//
			if (col == null) {
				throw new RuntimeException(
						"unable to find AUTO_INCREMENT column from table " +
						em.getBaseTable());
			}
	
			ConstantColumnResolver cr = new ConstantColumnResolver(col);
			return cr;
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
	
}
