/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.EnumAssignment;
import com.appspot.relaxe.ParameterAssignment;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.impl.pg.PGValueAssignerFactory;
import com.appspot.relaxe.pg.pagila.types.MPAARating;
import com.appspot.relaxe.pg.pagila.types.MPAARatingHolder;
import com.appspot.relaxe.pg.pagila.types.MPAARatingType;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.PrimitiveType;

public class PagilaValueAssignerFactory 
	extends PGValueAssignerFactory {

	@Override
	public <T extends PrimitiveType<T>, H extends PrimitiveHolder<?, T, H>> ParameterAssignment create(H holder, DataType columnType) {
		ParameterAssignment pa = super.create(holder, columnType);
		
		if (pa == null) {			
			String n = columnType.getTypeName();
			
			if (MPAARatingType.TYPE.getName().equals(n)) {
				MPAARatingHolder rh = MPAARatingHolder.of(holder);				
				pa = new MPAARatingAssignment(rh);
			}			
		}
		
		return pa;
	}		
	
	
	private static class MPAARatingAssignment
		extends EnumAssignment<MPAARating, MPAARatingType, MPAARatingHolder> {
		
		public MPAARatingAssignment(MPAARatingHolder value) {
			super(value);
		}
				
		@Override
		protected MPAARatingType getType() {
			return MPAARatingType.TYPE;
		}				
	}	
}
