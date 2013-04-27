/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.io.Serializable;

import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.PrimitiveType;


public interface AssignmentFactory<V extends Serializable, T extends PrimitiveType<T>, H extends PrimitiveHolder<V, T, H>> {	
	ParameterAssignment create(H holder);
}
