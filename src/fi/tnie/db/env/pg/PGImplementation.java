/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.pg;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Comparator;

import fi.tnie.db.AbstractAttributeWriter;
import fi.tnie.db.DefaultAttributeWriterFactory;
import fi.tnie.db.DefaultValueExtractorFactory;
import fi.tnie.db.ResultSetColumnResolver;
import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.env.CatalogFactory;
import fi.tnie.db.env.DefaultImplementation;
import fi.tnie.db.env.GeneratedKeyHandler;
import fi.tnie.db.expr.DefaultSQLSyntax;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.InsertStatement;
import fi.tnie.db.expr.SQLSyntax;
import fi.tnie.db.expr.ddl.ColumnDefinition;
import fi.tnie.db.meta.impl.pg.PGEnvironment;
import fi.tnie.db.types.ReferenceType;

public class PGImplementation
	extends DefaultImplementation {

    private SQLSyntax syntax;
    private PGGeneratedKeyHandler generatedKeyHandler;
    private PGEnvironment environment;

	public PGImplementation() {
		environment = new PGEnvironment();
	}

	@Override
	public CatalogFactory catalogFactory() {
		return new PGCatalogFactory(this.environment);
	}

	@Override
	protected Comparator<Identifier> createIdentifierComparator() {
		return this.environment.createIdentifierComparator();
	}

    @Override
    public ColumnDefinition serialColumnDefinition(String columnName, boolean big) {
    	return this.environment.serialColumnDefinition(columnName, big);
    }

    private final class PGGeneratedKeyHandler implements GeneratedKeyHandler {
    	
		public PGGeneratedKeyHandler() {
			super();			
		}

		@Override
		public <
			A extends Attribute,
			R extends Reference,
			T extends ReferenceType<A, R, T, E, ?, ?, M>,
			E extends Entity<A, R, T, E, ?, ?, M>,
			M extends EntityMetaData<A, R, T, E, ?, ?, M>
		>
		void processGeneratedKeys(
				InsertStatement ins, E target, ResultSet rs) throws SQLException {
//				int cc = rs.getMetaData().getColumnCount();
//
////				logger().debug("getGeneratedKeys: ");
//		
//			if (rs.next()) {
				ResultSetMetaData meta = rs.getMetaData();
				M em = target.getMetaData();
											
				ResultSetMetaData rsmd = rs.getMetaData();
				
				int cc = rsmd.getColumnCount();
				DefaultAttributeWriterFactory wf = new DefaultAttributeWriterFactory();				
				ResultSetColumnResolver cr = new ResultSetColumnResolver(em.getBaseTable(), meta);
																												
				for (int i = 1; i <= cc; i++) {
					AbstractAttributeWriter<A, T, E, ?, ?, ?, ?> w = wf.createWriter(em, cr, i);
					
					if (w != null) {
						w.write(rs, target);
					}
				}
//			}
		}
	}


    @Override
    public String driverClassName() {
        return "org.postgresql.Driver";
    }


    public static class PGSyntax
        extends DefaultSQLSyntax {

    }

    @Override
    public SQLSyntax getSyntax() {
        if (syntax == null) {
            syntax = new PGSyntax();
        }

        return syntax;
    }

	@Override
	public GeneratedKeyHandler generatedKeyHandler() {
		if (generatedKeyHandler == null) {			
			generatedKeyHandler = new PGGeneratedKeyHandler();
		}

		return generatedKeyHandler;
	}

	public PGEnvironment getEnvironment() {
		return environment;
	}
	
	@Override
	protected DefaultValueExtractorFactory createValueExtractorFactory() {
		return new PGValueExtractorFactory();
	}
	
	public String createJdbcUrl(String host, String database) {
		return createJdbcUrl(host, 5432, database);		
	}
	
	public String createJdbcUrl(String host, int port, String database) {
		if (database == null) {
			throw new NullPointerException("database");
		}
		
		if (host == null) {
			host = "127.0.0.1"; 
		}
		
		return "jdbc:postgresql://" + host + ":" + port + "/" + database;				
	}
}
