/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

import fi.tnie.db.rpc.PrimitiveHolder;

public class DecimalType
	extends PrimitiveType<DecimalType> {
	
	public static final DecimalType TYPE = new DecimalType();
	
	private DecimalType() {		
	}
	
	@Override
	public int getSqlType() {
		return Type.DECIMAL;
	}

	@Override
	public PrimitiveHolder<?, DecimalType> nil() {
		return null; // TODO: implement transliterable Decimal & DecimalHolder
//		return DecimalHolder.NULL_HOLDER;
	}
}
