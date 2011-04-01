/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.pg;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Comparator;

import fi.tnie.db.DefaultValueExtractorFactory;
import fi.tnie.db.ExtractorMap;
import fi.tnie.db.ValueExtractorFactory;
import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
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
    	private ValueExtractorFactory extractorFactory;

		public PGGeneratedKeyHandler(ValueExtractorFactory vef) {
			super();
			this.extractorFactory = vef;
		}

		@Override
		public <
			A extends Attribute,
			R,
			T extends ReferenceType<T>,
			E extends Entity<A, R, T, E>
		>
		void processGeneratedKeys(
				InsertStatement ins, E target, ResultSet rs) throws SQLException {
//				int cc = rs.getMetaData().getColumnCount();
//
////				logger().debug("getGeneratedKeys: ");
//
			ResultSetMetaData meta = rs.getMetaData();
			EntityMetaData<A, R, T, E> em = target.getMetaData();

			ExtractorMap<A, R, T, E> xm =
				new ExtractorMap<A, R, T, E>(meta, em, extractorFactory);
//			List<A> keys = new ArrayList<A>();

			xm.extract(rs, target);
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
			ValueExtractorFactory vef = getValueExtractorFactory();
			generatedKeyHandler = new PGGeneratedKeyHandler(vef);
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
