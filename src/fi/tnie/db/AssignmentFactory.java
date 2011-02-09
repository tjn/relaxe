/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.io.Serializable;

import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;

public interface AssignmentFactory<V extends Serializable, T extends PrimitiveType<T>, H extends PrimitiveHolder<V, T>> {	
	ParameterAssignment create(H holder);
}
