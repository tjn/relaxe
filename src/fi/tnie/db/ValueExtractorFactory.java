/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public interface ValueExtractorFactory {
	
	/**
	 * 
	 * @param meta
	 * @param col
	 * @return
	 * @throws SQLException
	 */
	ValueExtractor<?, ? > createExtractor(ResultSetMetaData meta, int col) 
		throws SQLException;
}
