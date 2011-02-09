/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;

public interface ValueAssignerFactory {
//	<T extends PrimitiveType<T>, H extends PrimitiveHolder<?, T>>	
//	Assigner<T, H> get(H holder);
	
	
	<T extends PrimitiveType<T>, H extends PrimitiveHolder<?, T>>
	ParameterAssignment create(H holder);
}
