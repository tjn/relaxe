/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

public class DefaultValueExtractorFactory implements ValueExtractorFactory {

	private static Logger logger = Logger.getLogger(DefaultValueExtractorFactory.class);
	
	@Override
	public	
	ValueExtractor<?, ?, ?> createExtractor(ResultSetMetaData meta, int col) 
		throws SQLException {
		ValueExtractor<?, ?, ?> e = null;
		int sqltype = meta.getColumnType(col);
	
		switch (sqltype) {
			case Types.INTEGER:					
			case Types.SMALLINT:
			case Types.TINYINT:
				e = createIntegerExtractor(col);	
				break;
			case Types.VARCHAR:
				e = createVarcharExtractor(col);				
			case Types.CHAR:
				e = createCharExtractor(col);
				break;
			case Types.DATE:				
				e = createDateExtractor(col);
				break;
			case Types.TIMESTAMP:
				e = createTimestampExtractor(col);
				break;
			case Types.FLOAT:				
			case Types.DOUBLE:
				e = createDoubleExtractor(col);
				break;				
			default:
				// 
	//			e = new ObjectExtractor(colno);
				break;
			}
		
		
			logger().debug("createExtractor - exit " + meta.getColumnLabel(col) + ": " + sqltype + " => " + e);
			
			return e;
		}
	

	public IntegerExtractor createIntegerExtractor(int col) {
		return new IntegerExtractor(col);
	}
	
	public DoubleExtractor createDoubleExtractor(int col) {
		return new DoubleExtractor(col);
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
	
	
		private static Logger logger() {
			return DefaultValueExtractorFactory.logger;
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



//		<V extends Serializable, P extends PrimitiveType<P>, H extends PrimitiveHolder<V, P>>
//		ValueExtractor<V, P, H> createExtractor2(ResultSetMetaData meta, int col, P type) 
//			throws SQLException {
//			int sqltype = meta.getColumnType(col);
//									
//		}

}


