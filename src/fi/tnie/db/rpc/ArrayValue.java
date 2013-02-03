/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import java.io.Serializable;

public interface ArrayValue<E extends Serializable>
	extends Serializable {

	int size();
	E get(int index);	
	E[] toArray();
}