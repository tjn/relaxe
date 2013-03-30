/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class LongVarBinaryType
	extends PrimitiveType<LongVarBinaryType> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4534314769517177344L;
	
	public static final LongVarBinaryType TYPE = new LongVarBinaryType();
	
	private LongVarBinaryType() {
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.LONGVARBINARY;
	}
	
	@Override
	public LongVarBinaryType self() {
		return this;
	}	
}
