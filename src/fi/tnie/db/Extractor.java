/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Extractor {
	void extract(ResultSet rs) 
		throws SQLException;
}
