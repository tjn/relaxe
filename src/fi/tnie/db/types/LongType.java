/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class LongType
	extends PrimitiveType<LongType> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8385284776124293389L;
	public static final LongType TYPE = new LongType();
	
	private LongType() {		
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.BIGINT;
	}
	
	@Override
	public LongType self() {
		return this;
	}
}
