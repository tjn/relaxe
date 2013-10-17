/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.pg;

import java.sql.SQLException;
import java.sql.Types;

import com.appspot.relaxe.DefaultValueExtractorFactory;
import com.appspot.relaxe.IntervalExtractor;
import com.appspot.relaxe.ValueExtractor;
import com.appspot.relaxe.VarcharExtractor;
import com.appspot.relaxe.env.pg.PGTSVectorType;
import com.appspot.relaxe.env.pg.PGTextArrayType;
import com.appspot.relaxe.env.pg.PGTextType;
import com.appspot.relaxe.types.IntervalType;

public class PGValueExtractorFactory extends DefaultValueExtractorFactory {

//	private static Logger logger = Logger.getLogger(PGValueExtractorFactory.class);
	
	
//	@Override
//	protected ValueExtractor<?, ?, ?> createExtractor(int sqltype,
//			String typename, int col) throws SQLException {
//		// TODO Auto-generated method stub
//		return super.createExtractor(sqltype, typename, col);
//	}
	
	@Override
	public	
	ValueExtractor<?, ?, ?> createExtractor(int sqltype, String typename, int col) 
		throws SQLException {		
		
//		int sqltype = meta.getColumnType(col);
//		String ctn = meta.getColumnTypeName(col);
						
		ValueExtractor<?, ?, ?> ve = null;
				
		if (sqltype == Types.OTHER) {
			if (IntervalType.DayTime.TYPE.getName().equals(typename)) {
				ve = createDayTimeIntervalExtractor(col);	
			}
			
			if (IntervalType.YearMonth.TYPE.getName().equals(typename)) {
				ve = createYearMonthIntervalExtractor(col);
			}
			
			if (PGTSVectorType.TYPE.getName().equals(typename)) {
				ve = new VarcharExtractor(col);
			}
		}
		else if (sqltype == Types.ARRAY && PGTextArrayType.TYPE.getName().equals(typename)) {
			ve = new PGTextArrayExtractor(col);
		}
		else if (sqltype == Types.VARCHAR && PGTextType.TYPE.getName().equals(typename)) {
			ve = new PGTextExtractor(col);
		}
		else {
			ve = super.createExtractor(sqltype, typename, col);
		}	
		
		return ve;
	}

	/**
	 *   
	 */
	@Override
	public IntervalExtractor.DayTime createDayTimeIntervalExtractor(int col) {
		return new PGIntervalExtractor.DayTime(col);
	}


	@Override
	public IntervalExtractor.YearMonth createYearMonthIntervalExtractor(int col) {
		return new PGIntervalExtractor.YearMonth(col);
	}

}


