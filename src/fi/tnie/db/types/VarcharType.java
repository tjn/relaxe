/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class VarcharType
	extends PrimitiveType<VarcharType> {

	public static final VarcharType TYPE = new VarcharType();
	
	private VarcharType() {
	}
	
	@Override
	public int getSqlType() {
		return Type.VARCHAR;
	}
	
}
