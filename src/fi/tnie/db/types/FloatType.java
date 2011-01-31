/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

import fi.tnie.db.rpc.PrimitiveHolder;

public class FloatType
	extends PrimitiveType<FloatType> {
	
	public static final FloatType TYPE = new FloatType();
	
	private FloatType() {		
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.FLOAT;
	}
	

	@Override
	public PrimitiveHolder<?, FloatType> nil() {
		return null; // TODO: implement FloatHolder		
	}

}
