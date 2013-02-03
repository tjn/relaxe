/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class VarcharType
	extends PrimitiveType<VarcharType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3335101232181862680L;
	
	public static final VarcharType TYPE = new VarcharType();
	
	protected VarcharType() {
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.VARCHAR;
	}
	
	@Override
	public VarcharType self() {
		return this;
	}
}
