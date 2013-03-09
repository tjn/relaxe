/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.pg;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import fi.tnie.db.DefaultValueExtractorFactory;
import fi.tnie.db.IntervalExtractor;
import fi.tnie.db.ValueExtractor;

public class PGValueExtractorFactory extends DefaultValueExtractorFactory {

//	private static Logger logger = Logger.getLogger(PGValueExtractorFactory.class);
	
	@Override
	public	
	ValueExtractor<?, ?, ?> createExtractor(ResultSetMetaData meta, int col) 
		throws SQLException {		
		
		int sqltype = meta.getColumnType(col);
		String ctn = meta.getColumnTypeName(col);
						
		ValueExtractor<?, ?, ?> ve = null;
		
		
		if (sqltype == Types.OTHER && "interval".equals(ctn)) {		
			ve = createDayTimeIntervalExtractor(col);
		}
		else if (sqltype == Types.OTHER && "interval_ym".equals(ctn)) {				
			ve = createYearMonthIntervalExtractor(col);
		}		
		else if (sqltype == Types.ARRAY && PGTextArrayType.TYPE.getName().equals(ctn)) {
			ve = new PGTextArrayExtractor(col);
		}
		else {
			ve = super.createExtractor(meta, col);
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


