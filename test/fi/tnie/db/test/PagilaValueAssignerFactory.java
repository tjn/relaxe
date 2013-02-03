/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.test;

import java.sql.Array;
import fi.tnie.db.ArrayAssignment;
import fi.tnie.db.DefaultValueAssignerFactory;
import fi.tnie.db.EnumAssignment;
import fi.tnie.db.ParameterAssignment;
import fi.tnie.db.meta.DataType;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.StringArray;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.VarcharType;

public class PagilaValueAssignerFactory 
	extends DefaultValueAssignerFactory {

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
		
		if (pa == null) {			
			String n = columnType.getTypeName();
			
			if (PGTextArrayType.TYPE.getName().equals(n)) {
				PGTextArrayHolder rh = PGTextArrayHolder.of(holder);				
				pa = new PGTextArrayAssignment(rh);
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
	
	private static class PGTextArrayAssignment
		extends ArrayAssignment<String, VarcharType, StringArray, PGTextArrayType, PGTextArrayHolder> {
		
		public PGTextArrayAssignment(PGTextArrayHolder value) {
			super(value);
		}
				
		@Override
		protected PGTextArrayType getType() {
			return PGTextArrayType.TYPE;
		}
		
		@Override
		protected void close(Array array) {
			// free not implemented in PG
		}
	}
}
