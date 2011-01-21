/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.VarcharType;

public class VarcharValue<A extends Serializable, E extends Entity<A, ?, ?, E>>
	extends AbstractValue<A, String, VarcharType, VarcharHolder, E, VarcharKey<A, E>> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4034249028279108750L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private VarcharValue() {
	}
	
	public VarcharValue(VarcharKey<A, E> k) {
		super(k);
	}
		
	@Override
	public void set(String newValue) {
		setHolder(VarcharHolder.valueOf(newValue));
	}	
}