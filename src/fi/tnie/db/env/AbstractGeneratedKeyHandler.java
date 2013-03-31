/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import fi.tnie.db.ResultSetColumnResolver;
import fi.tnie.db.ValueExtractor;
import fi.tnie.db.ValueExtractorFactory;
import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.ColumnResolver;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.expr.InsertStatement;
import fi.tnie.db.meta.Column;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public abstract class AbstractGeneratedKeyHandler
	implements GeneratedKeyHandler {
	
	private static Logger logger = Logger.getLogger(AbstractGeneratedKeyHandler.class);
	
	@Override
	public <
		A extends Attribute,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, ?, ?, M, ?>,
		E extends Entity<A, R, T, E, ?, ?, M, ?>,
		M extends EntityMetaData<A, R, T, E, ?, ?, M, ?>
	>
	void processGeneratedKeys(InsertStatement ins, E target, ResultSet rs) throws SQLException {		
		logger().debug("processGeneratedKeys - enter");
				
		ResultSetMetaData meta = rs.getMetaData();
		M em = target.getMetaData();
		
		logger().debug("processGeneratedKeys: em.getBaseTable().getQualifiedName()=" + em.getBaseTable().getQualifiedName());
									
		ResultSetMetaData rsmd = rs.getMetaData();
		
		int cc = rsmd.getColumnCount();		
				
		for (int colno = 1; colno <= cc; colno++) {
			logger().debug("colno=" + colno);
			logger().debug("rsmd.getColumnName: " + rsmd.getColumnName(colno));
			logger().debug("rsmd.getColumnLabel: " + rsmd.getColumnLabel(colno));
		}
		
		ColumnResolver cr = createColumnResolver(meta, em);		
		ValueExtractorFactory vef = getValueExtractorFactory();
		
		for (int colno = 1; colno <= cc; colno++) {
			logger().debug("colno=" + colno);
			Column rc = cr.getColumn(colno);
			logger().debug("cr.getColumn: " + rc);			
			logger().debug("rc.getColumnName(): " + (rc == null ? null : rc.getColumnName()));
		}
																										
		for (int i = 1; i <= cc; i++) {			
			A a = em.getAttribute(cr.getColumn(i));
			
			if (a != null) {
				PrimitiveKey<A, E, ?, ?, ?, ?> k = em.getKey(a);
				logger().debug("a => k: " + (a) + " => " + k);
				
					// ValueExtractor<?, ?, ?> ve = vef.createExtractor(rsmd, i);
					DataType type = ac.getDataType();
					ValueExtractor<?, ?, ?> ve = vef.createExtractor(type, i);
			}
		}
		
		logger().debug("processGeneratedKeys - exit");
	}

	protected 
	<
		A extends Attribute,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, ?, ?, M, ?>,
		E extends Entity<A, R, T, E, ?, ?, M, ?>,
		M extends EntityMetaData<A, R, T, E, ?, ?, M, ?>
	>
	ColumnResolver createColumnResolver(ResultSetMetaData meta, M em) {
		return new ResultSetColumnResolver(em.getBaseTable(), meta);		
	}
		
	protected abstract ValueExtractorFactory getValueExtractorFactory();


	protected <
		A extends Attribute,
		E extends Entity<A, ?, ?, E, ?, ?, ?, ?>,
		V extends Serializable,
		P extends PrimitiveType<P>,
		VH extends PrimitiveHolder<V, P, VH>,	
		VK extends PrimitiveKey<A, E, V, P, VH, VK>
	>
	void write(final VK key, final ValueExtractor<?, ?, ?> ve, ResultSet src, E dest) throws SQLException {
		PrimitiveHolder<?, ?, ?> v = ve.extractValue(src);
		VH vh = key.as(v);			
		key.set(dest, vh);
	}

	
	private static Logger logger() {
		return AbstractGeneratedKeyHandler.logger;
	}
}
