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
/**
 * 
 */
package com.appspot.relaxe;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.ent.MutableDataObject;
import com.appspot.relaxe.exec.QueryProcessorAdapter;
import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.query.QueryException;


public abstract class DataObjectProcessor<O extends MutableDataObject>
	extends QueryProcessorAdapter {

	private static Logger logger = LoggerFactory.getLogger(DataObjectProcessor.class);
				
	protected MutableDataObject.MetaData meta;
	private ValueExtractor<?, ?, ?>[] extractors;
	private ValueExtractorFactory valueExtractorFactory;
		
	public DataObjectProcessor(ValueExtractorFactory vef, QueryExpression qe) 
		throws QueryException {
		if (vef == null) {
			throw new NullPointerException("vef");
		}
		
		if (qe == null) {
			throw new NullPointerException("qe");
		}
										
		this.meta = new MutableDataObject.MetaData(qe);
		this.valueExtractorFactory = vef;
	}

	
	@Override
	public void startResultSet(ResultSetMetaData m) throws QueryException, SQLException, IOException {
		ValueExtractorFactory vef = this.valueExtractorFactory;
		
		int cc = m.getColumnCount();
		ValueExtractor<?, ?, ?>[] xa = new ValueExtractor<?, ?, ?>[cc];
		
		for (int i = 1; i <= cc; i++) {
			String col = m.getColumnLabel(i);
			
//			int t = m.getColumnType(i);
//			String tn = m.getColumnTypeName(i);
			
			ValueExtractor<?, ?, ?> ve = vef.createExtractor(m, i);
			
			if (ve == null) {
				ve = vef.createExtractor(m, i);
				
				throw new NullPointerException(
						"no extractor for column " + col + ":" + m.getColumnType(i) + "; " + m.getColumnTypeName(i) + ", vef: " + vef); 
			}
			
			xa[i - 1] = ve;
		}
						
		this.extractors = xa;	
	}
			
	@Override
	public void process(ResultSet rs, long ordinal) throws QueryException, SQLException, IOException {
		
		
		ValueExtractor<?, ?, ?> ve = null;
		
		try {
			O o = get();
			
			int count = this.extractors.length;
			
			for (int i = 0; i < count; i++) {
				ve = this.extractors[i];					
				o.set(i, ve.extract(rs));
			}			
			
			put(o);
		}
		catch (SQLException e) {
			StringBuilder buf = new StringBuilder();
			
			buf.append(" col: ").append(ve.getColumn());
			buf.append(" ordinal: ").append(ordinal);
			
			try {
				buf.append(" value: ").append(rs.getObject(ve.getColumn()));
			}
			catch (SQLException ie) {				
			}			
						
			buf.append(e.getMessage());			
			
			logger().error(buf.toString(), e);
			throw new QueryException(e.getMessage(), e);
		}
	}

	protected void put(O o)
		throws QueryException, IOException {
	}
	
	protected abstract O get();

	public MutableDataObject.MetaData getMetaData() {
		return meta;
	}

	private static Logger logger() {
		return DataObjectProcessor.logger;
	}	
}