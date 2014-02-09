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
package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.EnumAssignment;
import com.appspot.relaxe.ParameterAssignment;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.pg.pagila.types.MPAARating;
import com.appspot.relaxe.pg.pagila.types.MPAARatingHolder;
import com.appspot.relaxe.pg.pagila.types.MPAARatingType;
import com.appspot.relaxe.rdbms.pg.PGValueAssignerFactory;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.ValueHolder;

public class PagilaValueAssignerFactory 
	extends PGValueAssignerFactory {

	@Override
	public <T extends ValueType<T>, H extends ValueHolder<?, T, H>> ParameterAssignment create(H holder, DataType columnType) {
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
