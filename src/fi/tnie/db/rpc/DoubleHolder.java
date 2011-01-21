/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import fi.tnie.db.types.DoubleType;
import fi.tnie.db.types.Type;


public class DoubleHolder
	extends PrimitiveHolder<Double, DoubleType> {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = -8533627613502905762L;
	/**
	 * 
	 */	
	private Double value;	
	public static final DoubleHolder NULL_HOLDER = new DoubleHolder();
	public static final DoubleHolder ZERO = new DoubleHolder(0);
	public static final DoubleHolder ONE = new DoubleHolder(1);
	
	public static DoubleHolder valueOf(double v) {
		return 
			(v == 0) ? ZERO : 
			(v == 1) ? ONE :
			DoubleHolder.valueOf(Double.valueOf(v));
	}
	
	public static DoubleHolder valueOf(Double v) {
		return (v == null) ? NULL_HOLDER : new DoubleHolder(v);
	}
	
	public DoubleHolder(double value) {
		this.value = Double.valueOf(value);		
	}
	
	protected DoubleHolder() {		
	}
	
	@Override
	public Double value() {
		return this.value;
	}

	@Override
	public DoubleType getType() {
		return DoubleType.TYPE;
	}

	@Override
	public int getSqlType() {
		return Type.INTEGER;
	}
}
