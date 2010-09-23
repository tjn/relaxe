/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import fi.tnie.db.Entity;
import fi.tnie.db.EntityMetaData;
import fi.tnie.db.ExtractorMap;
import fi.tnie.db.FoldingComparator;
import fi.tnie.db.Identifiable;
import fi.tnie.db.expr.DefaultSQLSyntax;
import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.InsertStatement;
import fi.tnie.db.expr.SQLSyntax;
import fi.tnie.db.expr.SimpleElement;
import fi.tnie.db.expr.Token;
import fi.tnie.db.expr.VisitContext;
import fi.tnie.db.expr.ddl.ColumnDataType;
import fi.tnie.db.expr.ddl.ColumnDefinition;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.CatalogFactory;
import fi.tnie.db.meta.GeneratedKeyHandler;
import fi.tnie.db.meta.impl.ColumnMap;
import fi.tnie.db.meta.impl.DefaultEnvironment;

public class PGEnvironment
	extends DefaultEnvironment {
    
    private SQLSyntax syntax;
    
    private PGGeneratedKeyHandler generatedKeyHandler;

	public PGEnvironment() {
	}

	@Override
	public CatalogFactory catalogFactory() {	
		return new PGCatalogFactory(this);
	}

	@Override
	protected Comparator<Identifier> createIdentifierComparator() {
		return new FoldingComparator() {
			@Override
			protected String fold(String ordinaryIdentifier) {
				return ordinaryIdentifier.toLowerCase();
			}			
		};
	}

    @Override
    public ColumnDefinition serialColumnDefinition(String columnName, boolean big) {
        // TODO: support big:     
        Identifier n = createIdentifier(columnName);                
        return new ColumnDefinition(n, new Serial());
    }
    
    private final class PGGeneratedKeyHandler implements GeneratedKeyHandler {
		@Override
		public <
			A extends Enum<A> & Identifiable, 
			R extends Enum<R> & Identifiable, 
			Q extends Enum<Q> & Identifiable, 
			E extends Entity<A, R, Q, ? extends E>
		> 
		void processGeneratedKeys(
				InsertStatement ins, E target, ResultSet rs) throws SQLException {
//				int cc = rs.getMetaData().getColumnCount();
//				
////				logger().debug("getGeneratedKeys: ");
//				
			ResultSetMetaData meta = rs.getMetaData();
//				
//				for (int i = 1; i <= cc; i++) {
//				    String label = meta.getColumnLabel(i);
////				    logger().debug("[" + i + "]: " + label);
//                }

			EntityMetaData<A, R, Q, ? extends E> em = target.getMetaData();
			
			ExtractorMap<A, R, Q, E> xm = new ExtractorMap<A, R, Q, E>(meta, em);				
//			List<A> keys = new ArrayList<A>();
							
			xm.extract(rs, target);
										
//				for (int i = 1; i <= cc; i++) {
//					String name = rs.getMetaData().getColumnLabel(i);					
//					Column col = cm.get(name);
//					
//					if (col == null) {
//						throw new NullPointerException("Can not find column " + name + " in table: " + table.getQualifiedName());
//					} 
//					
//					A attr = em.getAttribute(col);				
//					keys.add(attr);				
//					
//	//				buf.append(name);
//	//				buf.append("\t");
//				}
//				
//				
//								
//				for (int i = 1; i <= cc; i++) {					
//					A attr = keys.get(i - 1);
//					
//					if (attr != null) {
//						Object value = rs.getObject(i);
//						target.set(attr, value);
//					}
//															
////					String col = rs.getString(i);
////					buf.append(col);
////					buf.append("\t");
//				}
			
		}
	}

	private static class Serial
        extends SimpleElement
        implements ColumnDataType, Token {

        @Override
        public String getTerminalSymbol() {
            return "serial";
        }

        @Override
        public void traverse(VisitContext vc, ElementVisitor v) {
            v.start(vc, this);
            v.end(this);
        }

        @Override
        public boolean isOrdinary() {
            return true;
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
}
