/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import java.io.Serializable;

import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.PrimitiveType;

public interface Parameter<
	V extends Serializable,
	T extends PrimitiveType<T>, 
	H extends PrimitiveHolder<V, T, H>
>
	extends ValueExpression, Token {

	String getName();
	H getValue();
	DataType getColumnType();
	
	boolean isMutable();

}
