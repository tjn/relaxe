/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import com.appspot.relaxe.rpc.CharHolder;
import com.appspot.relaxe.types.CharType;

public class CharAssignment
	extends StringAssignment<CharType, CharHolder> {

	public CharAssignment(CharHolder value) {
		super(value);
	}
}
