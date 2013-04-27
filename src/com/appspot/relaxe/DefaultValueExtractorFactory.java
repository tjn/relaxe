/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.appspot.relaxe.meta.DataType;


public class DefaultValueExtractorFactory implements ValueExtractorFactory {

	private static Logger logger = Logger.getLogger(DefaultValueExtractorFactory.class);
	
	@Override
	public final ValueExtractor<?, ?, ?> createExtractor(DataType dataType, int col)
			throws SQLException {
		int sqltype = dataType.getDataType();
		String typeName = dataType.getTypeName();
		return createExtractor(sqltype, typeName, col);
	}
	
	@Override
	public final ValueExtractor<?, ?, ?> createExtractor(ResultSetMetaData meta, int col) 
		throws SQLException {		
		int sqltype = meta.getColumnType(col);
		String typeName = meta.getColumnTypeName(col);
		return createExtractor(sqltype, typeName, col);
	}
			
	protected ValueExtractor<?, ?, ?> createExtractor(int sqltype, String typename, int col) 
		throws SQLException {
		
		ValueExtractor<?, ?, ?> e = null;
	
		switch (sqltype) {
			case Types.INTEGER:					
			case Types.SMALLINT:
			case Types.TINYINT:
				e = createIntegerExtractor(col);	
				break;
			case Types.BIGINT:
				e = createLongExtractor(col);
				break;				
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
				e = createVarcharExtractor(col);
				break;
			case Types.CHAR:			
				e = createCharExtractor(col);
				break;
			case Types.DATE:				
				e = createDateExtractor(col);
				break;
			case Types.TIMESTAMP:
				e = createTimestampExtractor(col);
				break;
			case Types.TIME:
				e = createTimeExtractor(col);
				break;
			case Types.NUMERIC:
			case Types.DECIMAL:
				e = createDecimalExtractor(col);
				break;		
			case Types.REAL:
			case Types.FLOAT:
				// TODO: treat real and float as Floats
			case Types.DOUBLE:
				e = createDoubleExtractor(col);
				break;			
			case Types.BINARY:
			case Types.VARBINARY:				
			case Types.LONGVARBINARY:
				e = createLongVarBinaryExtractor(col);
				break;
			default:
				logger().info("createExtractor: type=" + sqltype);
				logger().info("createExtractor: typename=" + typename);
				break;
			}
			
			return e;
		}
	

	public IntegerExtractor createIntegerExtractor(int col) {
		return IntegerExtractor.forColumn(col);
	}
	
	public DoubleExtractor createDoubleExtractor(int col) {
		return new DoubleExtractor(col);
	}
	
	public DecimalExtractor createDecimalExtractor(int col) {
		return new DecimalExtractor(col);
	}
	
	public VarcharExtractor createVarcharExtractor(int col) {
		return new VarcharExtractor(col);
	}
	
	public CharExtractor createCharExtractor(int col) {
		return new CharExtractor(col);
	}

	@Override
	public DateExtractor createDateExtractor(int col) {
		return new DateExtractor(col);
	}
	
	public TimestampExtractor createTimestampExtractor(int col) {
		return new TimestampExtractor(col);
	}
	
	public TimeExtractor createTimeExtractor(int col) {
		return new TimeExtractor(col);
	}
	
	public LongExtractor createLongExtractor(int col) {
		return new LongExtractor(col);
	}
	
	public LongVarBinaryExtractor createLongVarBinaryExtractor(int col) {
		return new LongVarBinaryExtractor(col);
	}
	
		
	/**
	 * Returns <code>null</code>. Default factory  
	 */
	@Override
	public IntervalExtractor.DayTime createDayTimeIntervalExtractor(int col) {
		return new SQLIntervalExtractor.DayTime(col);
	}


	@Override
	public IntervalExtractor.YearMonth createYearMonthIntervalExtractor(int col) {
		return new SQLIntervalExtractor.YearMonth(col);
	}

	private static Logger logger() {
		return DefaultValueExtractorFactory.logger;
	}


//		<V extends Serializable, P extends PrimitiveType<P>, H extends PrimitiveHolder<V, P>>
//		ValueExtractor<V, P, H> createExtractor2(ResultSetMetaData meta, int col, P type) 
//			throws SQLException {
//			int sqltype = meta.getColumnType(col);
//									
//		}

}


