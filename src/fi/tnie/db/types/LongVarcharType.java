/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class LongVarcharType
	extends PrimitiveType<LongVarcharType> {

	public static final LongVarcharType TYPE = new LongVarcharType();
	
	private LongVarcharType() {
		super(Type.LONGVARCHAR);
	}
}
