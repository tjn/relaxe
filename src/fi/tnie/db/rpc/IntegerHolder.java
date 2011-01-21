/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import fi.tnie.db.types.IntegerType;
import fi.tnie.db.types.Type;


public class IntegerHolder
	extends PrimitiveHolder<Integer, IntegerType> {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = -7895663698067736815L;
	private Integer value;	
	public static final IntegerHolder NULL_HOLDER = new IntegerHolder();
	
//	public static final IntegerType TYPE = IntegerType.TYPE;
	
	public static IntegerHolder valueOf(int v) {
		/**
		 * Possibly cache small ints later like java.lang.Integer
		 */
		return new IntegerHolder(v);
	}
	
	public static IntegerHolder valueOf(Integer v) {
		return v == null ? NULL_HOLDER : valueOf(v.intValue());
	}
	
	public IntegerHolder(int value) {
		this.value = Integer.valueOf(value);
	}
	
	protected IntegerHolder() {		
	}
	
	@Override
	public Integer value() {
		return this.value;
	}

	@Override
	public IntegerType getType() {
		return IntegerType.TYPE;
	}

	@Override
	public int getSqlType() {
		return Type.INTEGER;
	}
}
