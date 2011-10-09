/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.io.Serializable;

import fi.tnie.db.ent.FetchOptions;

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
