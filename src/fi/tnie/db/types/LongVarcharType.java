/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class LongVarcharType
	extends PrimitiveType<LongVarcharType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3990820153117029057L;
	public static final LongVarcharType TYPE = new LongVarcharType();
	
	private LongVarcharType() {
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.LONGVARCHAR;
	}
	
	@Override
	public LongVarcharType self() {
		return this;
	}
}
