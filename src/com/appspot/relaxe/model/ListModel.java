/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model;

import java.util.List;


public interface ListModel<E>
	extends ValueModel<List<E>> {
	
	boolean isEmpty();
}
