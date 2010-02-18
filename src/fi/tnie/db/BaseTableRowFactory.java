/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface BaseTableRowFactory<C extends Enum<C> & Identifiable, R extends BaseTableRow<C>, I extends InstantiationContext<R>>
	extends RowFactory<R, I>
{	
	@Override
	R newInstance(I ic) 
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

