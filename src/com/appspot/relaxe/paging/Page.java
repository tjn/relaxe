/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

import java.io.Serializable;
import java.util.List;

import com.appspot.relaxe.ent.FetchOptions;


public interface Page<E>
	extends Serializable {
	
	long offset();
	List<E> getContent();	
	FetchOptions options();	
	Long available();
}
