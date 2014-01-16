/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe.env;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.ResultSetColumnResolver;
import com.appspot.relaxe.ValueExtractor;
import com.appspot.relaxe.ValueExtractorFactory;
import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.ColumnResolver;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.value.ValueAttribute;
import com.appspot.relaxe.expr.InsertStatement;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ValueHolder;


public abstract class AbstractGeneratedKeyHandler
	implements GeneratedKeyHandler {
	
	private static Logger logger = LoggerFactory.getLogger(AbstractGeneratedKeyHandler.class);
	
	@Override
	public <
		A extends AttributeName,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, ?, ?, M>,
		E extends Entity<A, R, T, E, ?, ?, M>,
		M extends EntityMetaData<A, R, T, E, ?, ?, M>
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
					ValueAttribute<A, E, ?, ?, ?, ?> k = em.getKey(a);
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
		A extends AttributeName,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, ?, ?, M>,
		E extends Entity<A, R, T, E, ?, ?, M>,
		M extends EntityMetaData<A, R, T, E, ?, ?, M>
	>
	ColumnResolver createColumnResolver(ResultSetMetaData meta, M em) {
		return new ResultSetColumnResolver(em.getBaseTable(), meta);		
	}
		
	protected abstract ValueExtractorFactory getValueExtractorFactory();


	protected <
		A extends AttributeName,
		E extends Entity<?, ?, ?, ?, ?, ?, ?>,
		V extends Serializable,
		P extends ValueType<P>,
		VH extends ValueHolder<V, P, VH>,	
		VK extends ValueAttribute<A, E, V, P, VH, VK>
	>
	void write(final ValueAttribute<A, E, V, P, VH, VK> key, final ValueExtractor<?, ?, ?> ve, ResultSet src, E dest) throws SQLException {
		ValueHolder<?, ?, ?> v = ve.extractValue(src);
		VH vh = key.as(v);			
		key.set(dest, vh);
	}

	
	private static Logger logger() {
		return AbstractGeneratedKeyHandler.logger;
	}
}
