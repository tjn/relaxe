/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.PrimitiveType;

public interface ValueAssignerFactory {

	
	<	
		T extends PrimitiveType<T>, 
		H extends PrimitiveHolder<?, T, H>
	>
	ParameterAssignment create(H holder, DataType columnType);
}
