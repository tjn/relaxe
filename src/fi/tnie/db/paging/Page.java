/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.io.Serializable;
import java.util.List;

import fi.tnie.db.ent.FetchOptions;

public interface Page<E>
	extends Serializable {
	
	long offset();
	List<E> getContent();	
	FetchOptions options();	
	Long available();
}
