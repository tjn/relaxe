/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.test;

import fi.tnie.db.rpc.ArrayValue;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.StringArrayHolder;
import fi.tnie.db.types.VarcharType;

public class PGTextArrayHolder
	extends StringArrayHolder<VarcharType, PGTextArrayType, PGTextArrayHolder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1432615933967175087L;
	
	public static final PGTextArrayHolder NULL_HOLDER = new PGTextArrayHolder();
	
	public PGTextArrayHolder() {
		super();
	}

	public PGTextArrayHolder(String[] data) {
		super(data);	
	}
	
	public PGTextArrayHolder(ArrayValue<String> value) {
		super(value);
	}

	@Override
	public PGTextArrayHolder self() {
		return this;
	}

	@Override
	public PGTextArrayType getType() {
		return PGTextArrayType.TYPE;
	}

	public static PGTextArrayHolder newHolder(ArrayValue<String> newValue) {
		return new PGTextArrayHolder(newValue);
	}

	public static PGTextArrayHolder of(PrimitiveHolder<?, ?, ?> holder) {
		Object h = holder.self();
		PGTextArrayHolder mh = (PGTextArrayHolder) h;	
		return mh;
	}
}
