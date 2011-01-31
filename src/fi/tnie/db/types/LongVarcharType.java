/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

import fi.tnie.db.rpc.PrimitiveHolder;

public class LongVarcharType
	extends PrimitiveType<LongVarcharType> {

	public static final LongVarcharType TYPE = new LongVarcharType();
	
	private LongVarcharType() {
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.LONGNVARCHAR;
	}
	
	@Override
	public PrimitiveHolder<?, LongVarcharType> nil() {
		return null; // TODO: implement LongVarcharTypeHolder		
	}
	
}
