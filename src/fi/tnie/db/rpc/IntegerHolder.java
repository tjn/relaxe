/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import fi.tnie.db.types.IntegerType;
import fi.tnie.db.types.PrimitiveType;


public class IntegerHolder
	extends PrimitiveHolder<Integer, IntegerType> {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = -7895663698067736815L;
	private Integer value;	
	public static final IntegerHolder NULL_HOLDER = new IntegerHolder();
	private static final IntegerHolder.Factory FACTORY = new IntegerHolder.Factory();
	
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
	
	public static IntegerHolder.Factory factory() {
		return FACTORY;
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
		return PrimitiveType.INTEGER;
	}
	
	public static class Factory
		implements HolderFactory<Integer, IntegerType, IntegerHolder> {

		@Override
		public IntegerHolder newHolder(Integer value) {
			return IntegerHolder.valueOf(value);
		}
	}
}
