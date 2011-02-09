/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.VarcharType;

public class VarcharAssignment
	extends StringAssignment<VarcharType, VarcharHolder> {

	public VarcharAssignment(VarcharHolder value) {
		super(value);
	}
}
