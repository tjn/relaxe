/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Identifiable;
import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ColumnMap;
import fi.tnie.db.types.ReferenceType;

public class ExtractorMap<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	T extends ReferenceType<T>, 
	E extends Entity<A, R, Q, T, ? extends E>
> {
	
	private List<AttributeExtractor<A, R, Q, T, E>> attributeExctractorList;

	/**
	 * TODO: when initialized like this, this does not read references properly  
	 * @param rsmd
	 * @param em
	 * @throws SQLException
	 */
	public ExtractorMap(ResultSetMetaData rsmd, EntityMetaData<A, R, Q, T, ? extends E> em, ValueExtractorFactory vef) throws SQLException {		
		int cc = rsmd.getColumnCount();
		this.attributeExctractorList = new ArrayList<AttributeExtractor<A, R, Q, T, E>>(cc);
		
		ColumnMap cm = em.getBaseTable().columnMap();		
				
		for (int c = 1; c <= cc; c++) {			
			ValueExtractor<?, ?> ve = vef.createExtractor(rsmd, c);				
			String cl = rsmd.getColumnLabel(c);
			Column col = cm.get(cl);
						
			A a = em.getAttribute(col);
			
			if (a != null) {
				AttributeExtractor<A, R, Q, T, E> ae = new AttributeExtractor<A, R, Q, T, E>(a, ve);
				this.attributeExctractorList.add(ae);		
			}
			
//			Set<R> refs = em.getReferences(col);
//			
//			for (R r : refs) {
//				ForeignKey fk = em.getForeignKey(r);
//			}			
		}
	}
	
	public ExtractorMap(QueryExpression e, TableReference b) throws SQLException {
		// e.getTableExpr().getSelect().getSelectList()
//		e.getTableExpr().getSelect().get
	}
	
	public ExtractorMap(EntityQueryTask<A, R, Q, T, E> query) throws SQLException {
				
	}

	public void extract(ResultSet src, E e) throws SQLException {
		int ec = attributeExctractorList.size();
				
		for (int i = 0; i < ec; i++) {
			attributeExctractorList.get(i).extract(src, e);
		}
	}
}
