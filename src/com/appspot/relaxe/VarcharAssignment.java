/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import com.appspot.relaxe.rpc.VarcharHolder;
import com.appspot.relaxe.types.VarcharType;

public class VarcharAssignment
	extends StringAssignment<VarcharType, VarcharHolder> {

	public VarcharAssignment(VarcharHolder value) {
		super(value);
	}
}
