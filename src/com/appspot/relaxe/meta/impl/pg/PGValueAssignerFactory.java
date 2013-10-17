/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.pg;

import java.sql.Array;

import com.appspot.relaxe.ArrayAssignment;
import com.appspot.relaxe.DefaultValueAssignerFactory;
import com.appspot.relaxe.ParameterAssignment;
import com.appspot.relaxe.env.pg.PGTextArrayHolder;
import com.appspot.relaxe.env.pg.PGTextArrayType;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.rpc.StringArray;
import com.appspot.relaxe.types.PrimitiveType;
import com.appspot.relaxe.types.VarcharType;

public class PGValueAssignerFactory 
	extends DefaultValueAssignerFactory {

	@Override
	public <T extends PrimitiveType<T>, H extends PrimitiveHolder<?, T, H>> ParameterAssignment create(H holder, DataType columnType) {
		ParameterAssignment pa = super.create(holder, columnType);
		
		if (pa == null) {			
			String n = columnType.getTypeName();
			
			if (PGTextArrayType.TYPE.getName().equals(n)) {
				PGTextArrayHolder rh = PGTextArrayHolder.of(holder);				
				pa = new PGTextArrayAssignment(rh);
			}			
		}		
		
		return pa;
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
			// free() -method is not implemented in PG
		}
	}
}
