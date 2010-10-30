/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

public class DefaultValueExtractorFactory implements ValueExtractorFactory {

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
			case Types.CHAR:
				e = new VarcharExtractor(col);	
				break;					
			default:
				// 
	//			e = new ObjectExtractor(colno);
				break;
			}	
			
			return e;
		}
	}