/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.apache.log4j.Logger;

import com.appspot.relaxe.ent.MutableDataObject;
import com.appspot.relaxe.ent.QueryExpressionSource;
import com.appspot.relaxe.exec.QueryProcessorAdapter;
import com.appspot.relaxe.query.QueryException;


public abstract class DataObjectProcessor<O extends MutableDataObject>
	extends QueryProcessorAdapter {

	private static Logger logger = Logger.getLogger(DataObjectProcessor.class);
				
	protected MutableDataObject.MetaData meta;
	private ValueExtractor<?, ?, ?>[] extractors;
	private ValueExtractorFactory valueExtractorFactory;
		
	public DataObjectProcessor(ValueExtractorFactory vef, QueryExpressionSource qes) 
		throws QueryException {
		if (vef == null) {
			throw new NullPointerException("vef");
		}
		
		if (qes == null) {
			throw new NullPointerException("qo");
		}
										
		this.meta = new MutableDataObject.MetaData(qes);
		this.valueExtractorFactory = vef;
	}

	
	@Override
	public void startQuery(ResultSetMetaData m) throws QueryException, SQLException {
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
	public void process(ResultSet rs, long ordinal) throws QueryException {
		try {
			O o = get();			
			
			int count = this.extractors.length;
			
			for (int i = 0; i < count; i++) {
				ValueExtractor<?, ?, ?> ve = this.extractors[i];					
				o.set(i, ve.extractValue(rs));
			}			
			
			put(o);																
		}
		catch (Throwable e) {
			logger().error(e.getMessage(), e);
			throw new QueryException(e.getMessage(), e);
		}
	}

	protected void put(O o) {
	}
	
	protected abstract O get();

	public MutableDataObject.MetaData getMetaData() {
		return meta;
	}

	private static Logger logger() {
		return DataObjectProcessor.logger;
	}	
}