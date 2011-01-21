/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.rpc.PrimitiveHolder;

public class IntegerType
	extends PrimitiveType<IntegerType> {
	
	public static final IntegerType TYPE = new IntegerType();
	
	private IntegerType() {
	}
	
	@Override
	public int getSqlType() {
		return Type.INTEGER;
	}
	
	@Override
	public PrimitiveHolder<?, IntegerType> nil() {
		return IntegerHolder.NULL_HOLDER;
	}	
	
		
}
