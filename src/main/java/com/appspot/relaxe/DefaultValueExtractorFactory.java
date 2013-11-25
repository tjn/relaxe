/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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
package com.appspot.relaxe;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.appspot.relaxe.meta.DataType;


public class DefaultValueExtractorFactory implements ValueExtractorFactory {

	private static Logger logger = LoggerFactory.getLogger(DefaultValueExtractorFactory.class);
	
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
	

	@Override
	public IntegerExtractor createIntegerExtractor(int col) {
		return IntegerExtractor.forColumn(col);
	}
	
	@Override
	public DoubleExtractor createDoubleExtractor(int col) {
		return new DoubleExtractor(col);
	}
	
	public DecimalExtractor createDecimalExtractor(int col) {
		return new DecimalExtractor(col);
	}
	
	@Override
	public VarcharExtractor createVarcharExtractor(int col) {
		return new VarcharExtractor(col);
	}
	
	@Override
	public CharExtractor createCharExtractor(int col) {
		return new CharExtractor(col);
	}

	@Override
	public DateExtractor createDateExtractor(int col) {
		return new DateExtractor(col);
	}
	
	@Override
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


//		<V extends Serializable, P extends AbstractPrimitiveType<P>, H extends AbstractPrimitiveHolder<V, P>>
//		ValueExtractor<V, P, H> createExtractor2(ResultSetMetaData meta, int col, P type) 
//			throws SQLException {
//			int sqltype = meta.getColumnType(col);
//									
//		}

}


