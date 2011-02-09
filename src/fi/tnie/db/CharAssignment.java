/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.types.CharType;

public class CharAssignment
	extends StringAssignment<CharType, CharHolder> {

	public CharAssignment(CharHolder value) {
		super(value);
	}
}
