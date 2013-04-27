/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.appspot.relaxe.meta.DataType;


public interface ValueExtractorFactory {
	
	/**
	 * 
	 * @param meta
	 * @param col
	 * @return
	 * @throws SQLException
	 */
	ValueExtractor<?, ?, ?> createExtractor(ResultSetMetaData meta, int col) 
		throws SQLException;
	
	ValueExtractor<?, ?, ?> createExtractor(DataType dataType, int col) 
		throws SQLException;

	
	IntegerExtractor createIntegerExtractor(int col);
	VarcharExtractor createVarcharExtractor(int col);
	DateExtractor createDateExtractor(int col);
	CharExtractor createCharExtractor(int col);
	DoubleExtractor createDoubleExtractor(int col);
	TimestampExtractor createTimestampExtractor(int col);
	
	IntervalExtractor.YearMonth createYearMonthIntervalExtractor(int col);
	IntervalExtractor.DayTime createDayTimeIntervalExtractor(int col);
}
