/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.io.Serializable;

import com.appspot.relaxe.rpc.AbstractPrimitiveHolder;
import com.appspot.relaxe.types.AbstractPrimitiveType;


public interface AssignmentFactory<V extends Serializable, T extends AbstractPrimitiveType<T>, H extends AbstractPrimitiveHolder<V, T, H>> {	
	ParameterAssignment create(H holder);
}
