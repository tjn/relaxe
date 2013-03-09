/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.test;

import fi.tnie.db.EnumAssignment;
import fi.tnie.db.ParameterAssignment;
import fi.tnie.db.env.pg.PGValueAssignerFactory;
import fi.tnie.db.meta.DataType;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;

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
