/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

import java.io.Serializable;

import com.appspot.relaxe.ent.FetchOptions;


public interface ResultPage
	extends Serializable {
	
	long getOffset();
	int size();
	
	/*
	 * 
	 */
	Boolean isLastPage();
	
	Long available();
	
	/**
	 * Returns the options which were used when this result page was fetched.
	 * @return
	 */
	FetchOptions getFetchOptions();
}
