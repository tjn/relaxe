/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.pg;

import java.util.Comparator;

import fi.tnie.db.DefaultValueExtractorFactory;
import fi.tnie.db.ValueExtractorFactory;
import fi.tnie.db.env.CatalogFactory;
import fi.tnie.db.env.AbstractGeneratedKeyHandler;
import fi.tnie.db.env.DefaultImplementation;
import fi.tnie.db.expr.DefaultSQLSyntax;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.SQLSyntax;
import fi.tnie.db.expr.ddl.ColumnDefinition;
import fi.tnie.db.meta.SerializableEnvironment;
import fi.tnie.db.meta.impl.pg.PGEnvironment;

public class PGImplementation
	extends DefaultImplementation<PGImplementation> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7298329938853171504L;
	private SQLSyntax syntax;    
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
    
//    public ColumnDefinition serialColumnDefinition(String columnName, boolean big) {
//    	return this.environment.serialColumnDefinition(columnName, big);
//    }
    
    public static class PGGeneratedKeyHandler 
    	extends AbstractGeneratedKeyHandler {
    	
    	private ValueExtractorFactory extractorFactory;

		public PGGeneratedKeyHandler(ValueExtractorFactory extractorFactory) {
			super();
			this.extractorFactory = extractorFactory;
		}
		
		@Override
		protected ValueExtractorFactory getValueExtractorFactory() {
			return this.extractorFactory;
		}
    }

//    public static class PGGeneratedKeyHandler implements GeneratedKeyHandler {
//    	
//    	private PersistenceContext context;
//    	
//		public PGGeneratedKeyHandler(PersistenceContext context) {
//			super();			
//			this.context = context;
//		}
//
//		@Override
//		public <
//			A extends Attribute,
//			R extends Reference,
//			T extends ReferenceType<A, R, T, E, ?, ?, M, ?>,
//			E extends Entity<A, R, T, E, ?, ?, M, ?>,
//			M extends EntityMetaData<A, R, T, E, ?, ?, M, ?>
//		>
//		void processGeneratedKeys(
//				InsertStatement ins, E target, ResultSet rs) throws SQLException {
//			ResultSetMetaData meta = rs.getMetaData();
//			M em = target.getMetaData();
//										
//			ResultSetMetaData rsmd = rs.getMetaData();
//			
//			int cc = rsmd.getColumnCount();
//			
//			AttributeWriterFactory wf = context.getAttributeWriterFactory();								
//			ResultSetColumnResolver cr = new ResultSetColumnResolver(em.getBaseTable(), meta);
//																											
//			for (int i = 1; i <= cc; i++) {
//				AbstractAttributeWriter<A, E> w = wf.createWriter(em, cr, i);
//				
//				if (w != null) {
//					w.write(rs, target);
//				}
//			}
//		}
//	}


    @Override
    public String defaultDriverClassName() {
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

	public PGEnvironment getEnvironment() {
		return environment;
	}
	
	@Override
	protected DefaultValueExtractorFactory createValueExtractorFactory() {
		return new PGValueExtractorFactory();
	}
	
	public String createJdbcUrl(String database) {
		return createJdbcUrl(null, database);		
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
	
	@Override
	public SerializableEnvironment environment() {
		return this.environment;		
	}

//	public java.sql.Driver getDriver() {		
//		if (driver == null) {
//			driver = new org.postgresql.Driver();
//		}
//				
//		return driver;
//	}

//	@Override
//	protected DefaultAttributeWriterFactory createAttributeWriterFactory() {
//		return new PGAttributeWriterFactory();
//	}


	@Override
	public PGImplementation self() {
		return this;
	}
}
