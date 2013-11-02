/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.hsqldb;

import com.appspot.relaxe.env.CatalogFactory;
import com.appspot.relaxe.env.DefaultImplementation;
import com.appspot.relaxe.env.hsqldb.expr.HSQLDBArrayTypeDefinition;
import com.appspot.relaxe.expr.DefaultSQLSyntax;
import com.appspot.relaxe.expr.SQLSyntax;
import com.appspot.relaxe.expr.ddl.types.SQLArrayTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.SQLTypeDefinition;
import com.appspot.relaxe.meta.SerializableEnvironment;
import com.appspot.relaxe.meta.impl.hsqldb.HSQLDBEnvironment;

public class HSQLDBImplementation
	extends DefaultImplementation<HSQLDBImplementation> {

	private SQLSyntax syntax;
	
	public HSQLDBImplementation() {
	}

	@Override
	public CatalogFactory catalogFactory() {		
		return new HSQLDBCatalogFactory(HSQLDBEnvironment.environment());
	}

//	@Override
//	protected Comparator<Identifier> createIdentifierComparator() {
//		return this.environment.createIdentifierComparator();
//	}

//    @Override
//    public ColumnDefinition serialColumnDefinition(String columnName, boolean big) {
//    	return this.environment.serialColumnDefinition(columnName, big);
//    }

//    private final class HSQLDBGeneratedKeyHandler implements GeneratedKeyHandler {
//    	
//		public HSQLDBGeneratedKeyHandler() {
//			super();			
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
////				int cc = rs.getMetaData().getColumnCount();
////
//////				logger().debug("getGeneratedKeys: ");
////		
////			if (rs.next()) {
//				ResultSetMetaData meta = rs.getMetaData();
//				M em = target.getMetaData();
//											
//				ResultSetMetaData rsmd = rs.getMetaData();
//				
//				int cc = rsmd.getColumnCount();								
//				ResultSetColumnResolver cr = new ResultSetColumnResolver(em.getBaseTable(), meta);
//				
//				ValueExtractorFactory vef = getValueExtractorFactory();
//				
//																												
//				for (int i = 1; i <= cc; i++) {
//					A a = em.getAttribute(cr.getColumn(i));					
//					PrimitiveKey<A, E, ?, ?, ?, ?> k = em.getKey(a);
//					
//					ValueExtractor<?, ?, ?> ve = vef.createExtractor(rsmd, i);
//					write(k.self(), ve, rs, target);
//					
////					AbstractAttributeWriter<A, E, ?, ?, ?, ?> w = wf.createWriter(em, cr, i);
////					
////					if (w != null) {
////						w.write(rs, target);
////					}
//				}
////			}
//		}
//		
//		public <
//			A extends Attribute,
//			E extends Entity<A, ?, ?, E, ?, ?, ?, ?>,
//			V extends Serializable,
//			P extends AbstractPrimitiveType<P>,
//			VH extends AbstractPrimitiveHolder<V, P, VH>,	
//			VK extends PrimitiveKey<A, E, V, P, VH, VK>
//		>
//		void write(final VK key, final ValueExtractor<?, ?, ?> ve, ResultSet src, E dest) throws SQLException {
//			AbstractPrimitiveHolder<?, ?, ?> v = ve.extractValue(src);
//			VH vh = key.as(v);			
//			key.set(dest, vh);
//		}
//		
//		
//		private <		
//			A extends Attribute,
//			E extends Entity<A, ?, ?, E, ?, ?, ?, ?>,
//			V extends Serializable,
//			P extends AbstractPrimitiveType<P>,
//			VH extends AbstractPrimitiveHolder<V, P, VH>,	
//			VK extends PrimitiveKey<A, E, V, P, VH, VK>
//		>
//		AttributeWriter<A, E> createWriter(final VK key, final ValueExtractor<?, ?, ?> ve) {
//			return new AttributeWriter<A, E>() {
//				@Override
//				public AbstractPrimitiveHolder<?, ?, ?> write(DataObject src, E dest)
//						throws EntityRuntimeException {
//					
//					ve.extractValue(rs);
//					AbstractPrimitiveHolder<?, ?, ?> h = src.get(index);
//					VH vc = key.as(h);				
//					key.set(dest, vc);
//					return vc;
//				}
//			};
//		}
//	    
//	}
    
    
	
	

    @Override
    public String defaultDriverClassName() {
        return "org.hsqldb.jdbcDriver";
    }


    public static class HSQLDBSyntax
        extends DefaultSQLSyntax {
    	
    	@Override
    	public SQLTypeDefinition newArrayTypeDefinition(SQLTypeDefinition elementType) {
    		return new HSQLDBArrayTypeDefinition(elementType, null);
    	}

    }

    @Override
    public SQLSyntax getSyntax() {
        if (syntax == null) {
            syntax = new HSQLDBSyntax();
        }

        return syntax;
    }


	public HSQLDBEnvironment getEnvironment() {
		return HSQLDBEnvironment.environment();
	}
	
	@Override
	public String createJdbcUrl(String database) {
		return createJdbcUrl(null, database);		
	}
	
	@Override
	public String createJdbcUrl(String host, String database) {
		return createJdbcUrl(host, -1, database);		
	}
	
	@Override
	public String createJdbcUrl(String host, int port, String database) {
		if (database == null) {
			throw new NullPointerException("database");
		}
		
		return "jdbc:hsqldb:" + database;				
	}
	
	@Override
	public SerializableEnvironment environment() {
		return HSQLDBEnvironment.environment();		
	}
		
//	public java.sql.Driver getDriver() {		
//		if (driver == null) {
//			driver = new org.postgresql.Driver();
//		}
//				
//		return driver;
//	}

//	@Override
//	protected AttributeWriterFactory createAttributeWriterFactory() {
//		return new PGAttributeWriterFactory();
//	}

	
	@Override
	public HSQLDBImplementation self() {
		return this;
	}
	
	
	
}
