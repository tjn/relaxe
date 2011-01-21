/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

import fi.tnie.db.rpc.DoubleHolder;
import fi.tnie.db.rpc.PrimitiveHolder;

public class DoubleType
	extends PrimitiveType<DoubleType> {
	
	public static final DoubleType TYPE = new DoubleType();
	
	private DoubleType() {	
	}
	
	@Override
	public int getSqlType() {
		return Type.DOUBLE;
	}
	

	@Override
	public PrimitiveHolder<?, DoubleType> nil() {
		return DoubleHolder.NULL_HOLDER;
	}	
}
