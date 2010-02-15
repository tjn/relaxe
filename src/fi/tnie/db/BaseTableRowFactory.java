/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface BaseTableRowFactory<C extends Enum<C> & Identifiable, R extends BaseTableRow<C>>
	extends RowFactory<R>
{	
	@Override
	R newInstance(InstantiationContext ic) 
		throws InstantiationException, IllegalAccessException;

		
	/**
	 * Reloads the row by using a primary key.  
	 * 
	 * @param predicate
	 * @return
	 */
	R reload(R pk, Connection c)
		throws SQLException;
	
}

