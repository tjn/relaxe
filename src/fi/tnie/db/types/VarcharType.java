/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.VarcharHolder;

public class VarcharType
	extends PrimitiveType<VarcharType> {

	public static final VarcharType TYPE = new VarcharType();
	
	private VarcharType() {
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.VARCHAR;
	}

	@Override
	public PrimitiveHolder<?, VarcharType> nil() {
		return VarcharHolder.NULL_HOLDER;
	}	
}
