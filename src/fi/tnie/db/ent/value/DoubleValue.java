/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.DoubleHolder;
import fi.tnie.db.types.DoubleType;

public class DoubleValue<A extends Attribute, E extends Entity<A, ?, ?, E>>
	extends AbstractValue<A, Double, DoubleType, DoubleHolder, E, DoubleKey<A, E>> {

	/**
	 *
	 */
	private static final long serialVersionUID = -5737849327578345205L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private DoubleValue() {
	}

	public DoubleValue(DoubleKey<A, E> k) {
		super(k);
	}

	@Override
	public void set(Double newValue) {
		setHolder(DoubleHolder.valueOf(newValue));
	}

}