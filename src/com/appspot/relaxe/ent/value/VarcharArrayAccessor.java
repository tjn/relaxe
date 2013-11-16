/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.StringArray;
import com.appspot.relaxe.rpc.VarcharArrayHolder;
import com.appspot.relaxe.types.VarcharArrayType;

public class VarcharArrayAccessor<
	A extends Attribute,
	E extends HasVarcharArray<A, E>
>
	extends AbstractPrimitiveAccessor<A, E, StringArray, VarcharArrayType, VarcharArrayHolder, VarcharArrayKey<A, E>> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -4859792482539290356L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private VarcharArrayAccessor() {
	}

	public VarcharArrayAccessor(E target, VarcharArrayKey<A, E> k) {
		super(target, k); 
	}
}