/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.meta.DataType;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;

public interface ValueAssignerFactory {

	
	<	
		T extends PrimitiveType<T>, 
		H extends PrimitiveHolder<?, T, H>
	>
	ParameterAssignment create(H holder, DataType columnType);
}
