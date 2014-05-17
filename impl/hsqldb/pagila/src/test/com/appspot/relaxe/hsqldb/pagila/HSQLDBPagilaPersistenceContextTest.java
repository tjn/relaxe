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
package com.appspot.relaxe.hsqldb.pagila;

import com.appspot.relaxe.ParameterAssignment;
import com.appspot.relaxe.ValueAssignerFactory;
import com.appspot.relaxe.env.pg.PGTextArrayHolder;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.DataTypeImpl;
import com.appspot.relaxe.value.StringArray;

import junit.framework.TestCase;

public class HSQLDBPagilaPersistenceContextTest extends TestCase {

	public void testGetValueAssignerFactory() {

		HSQLDBPagilaPersistenceContext pc = new HSQLDBPagilaPersistenceContext();
		
		ValueAssignerFactory vf = pc.getValueAssignerFactory();
		
		StringArray a = new StringArray(new String[] {"a", "b", "c"} );
		
		PGTextArrayHolder ah = PGTextArrayHolder.valueOf(a);
		
		DataType type = new DataTypeImpl(ah.getSqlType(), ah.getType().getName()); 
				
		ParameterAssignment pa = vf.create(ah, type);
		assertNotNull(pa);
		
	}

}
