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
	public ValueExtractor<?, ?> createExtractor(ResultSetMetaData meta, int col) 
		throws SQLException {
		ValueExtractor<?, ?> e = null;
		int sqltype = meta.getColumnType(col);
	
		switch (sqltype) {
			case Types.INTEGER:					
			case Types.SMALLINT:
			case Types.TINYINT:
				e = new IntExtractor(col);	
				break;
			case Types.VARCHAR:
				e = new VarcharExtractor(col);
			case Types.CHAR:
				e = new CharExtractor(col);	
				break;					
			case Types.DATE:
				e = new DateExtractor(col);	
				break;
			case Types.TIMESTAMP:
				e = new TimestampExtractor(col);	
				break;								
			default:
				// 
	//			e = new ObjectExtractor(colno);
				break;
			}
		
		
			logger().debug("createExtractor - exit " + meta.getColumnLabel(col) + ": " + sqltype + " => " + e);
			
			return e;
		}
	
	
		private static Logger logger() {
			return DefaultValueExtractorFactory.logger;
		}
	}