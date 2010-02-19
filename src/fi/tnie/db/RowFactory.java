/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import fi.tnie.db.expr.QueryExpression;


public interface RowFactory<R extends Row, I extends InstantiationContext<R>> {	
	R newInstance(I ictx, ResultSet rs) 
		throws InstantiationException, IllegalAccessException, SQLException;
	
	R newInstance(I ictx) 
		throws InstantiationException, IllegalAccessException;

	public I prepare(QueryExpression query);
	void finish(I ictx);
	
	
}
