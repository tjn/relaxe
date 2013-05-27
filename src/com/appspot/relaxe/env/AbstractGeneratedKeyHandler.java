/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.appspot.relaxe.ResultSetColumnResolver;
import com.appspot.relaxe.ValueExtractor;
import com.appspot.relaxe.ValueExtractorFactory;
import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.ColumnResolver;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.value.PrimitiveKey;
import com.appspot.relaxe.expr.InsertStatement;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.rpc.AbstractPrimitiveHolder;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.PrimitiveType;
import com.appspot.relaxe.types.ReferenceType;


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
	void processGeneratedKeys(InsertStatement ins, E target, Statement qs) throws SQLException {		
		logger().debug("processGeneratedKeys - enter");
		
		ResultSet rs = qs.getGeneratedKeys();
		
		if (rs.next()) {
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
				Column ac = cr.getColumn(i);
				A a = em.getAttribute(ac);
				
				if (a != null) {
					PrimitiveKey<A, E, ?, ?, ?, ?> k = em.getKey(a);
					logger().debug("a => k: " + (a) + " => " + k);
					
					// ValueExtractor<?, ?, ?> ve = vef.createExtractor(rsmd, i);
					DataType type = ac.getDataType();
					ValueExtractor<?, ?, ?> ve = vef.createExtractor(type, i);
					
					logger().debug("a => x: " + (a) + " => " + ve);
					write(k.self(), ve, rs, target);
				}
			}
			
			logger().debug("processGeneratedKeys - exit");
		}
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
	void write(final PrimitiveKey<A, E, V, P, VH, VK> key, final ValueExtractor<?, ?, ?> ve, ResultSet src, E dest) throws SQLException {
		AbstractPrimitiveHolder<?, ?, ?> v = ve.extractValue(src);
		VH vh = key.as(v);			
		key.set(dest, vh);
	}

	
	private static Logger logger() {
		return AbstractGeneratedKeyHandler.logger;
	}
}
