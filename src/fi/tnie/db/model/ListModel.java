/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

import java.util.List;


public interface ListModel<E>
	extends ValueModel<List<E>> {
	
	boolean isEmpty();
}
