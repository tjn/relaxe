/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.SQLException;

import com.appspot.relaxe.env.Implementation;
import com.appspot.relaxe.query.QueryException;


public interface HasTestContext<I extends Implementation<I>> {	
	public TestContext<I> getTestContext(I imp) throws SQLException, QueryException;
}
