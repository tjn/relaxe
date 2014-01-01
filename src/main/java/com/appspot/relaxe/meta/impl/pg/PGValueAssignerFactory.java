/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
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
