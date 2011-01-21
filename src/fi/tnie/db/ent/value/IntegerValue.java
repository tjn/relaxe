/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.types.IntegerType;

public class IntegerValue<A extends Serializable, E extends Entity<A, ?, ?, E>>
	extends AbstractValue<A, Integer, IntegerType, IntegerHolder, E, IntegerKey<A, E>> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2731889274638406123L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private IntegerValue() {
	}
	
	public IntegerValue(IntegerKey<A, E> k) {
		super(k);
	}
		
	@Override
	public void set(Integer newValue) {
		setHolder(IntegerHolder.valueOf(newValue));
	}
	
}