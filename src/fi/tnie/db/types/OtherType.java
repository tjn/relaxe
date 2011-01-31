/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

import fi.tnie.db.rpc.PrimitiveHolder;

public class OtherType
	extends PrimitiveType<OtherType> {
	
	public static final OtherType TYPE = new OtherType();
	
	private OtherType() {		
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.OTHER;
	}
	
	@Override
	public PrimitiveHolder<?, OtherType> nil() {
		return null; // TODO: implement OtherHolder		
	}
	
}
