/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.exec;

import com.appspot.relaxe.query.QueryException;


public interface ResultProcessor {
	public void prepare()
			throws QueryException;
	
	public void finish();

	
}
