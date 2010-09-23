/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public class IntHolder
	extends Holder {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7895663698067736815L;

	private Integer value;
	
	public static final IntHolder NULL_HOLDER = new IntHolder();
	
	public static IntHolder valueOf(int v) {
		/**
		 * Possibly cache small ints later
		 */
		return new IntHolder(v);
	}
	
	public IntHolder(int value) {
		this.value = Integer.valueOf(value);
	}
	
	private IntHolder() {		
	}
	
	public Integer value() {
		return this.value;
	}
}
