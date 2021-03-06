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

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.value.HasInteger;
import com.appspot.relaxe.ent.value.HasIntegerAttribute;
import com.appspot.relaxe.ent.value.IntegerAttribute;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Film;
import com.appspot.relaxe.types.IntegerType;

import junit.framework.TestCase;

public class PagilaIntegerKeyTest extends TestCase {

	public void testEquals() {		
		test(Film.Type.TYPE.getMetaData(), Film.Attribute.FILM_ID);
	}
	
	private 
	<
		A extends AttributeName,
		E extends HasInteger.Read<A, E, B>,
		B extends HasInteger.Write<A, E, B>,
		M extends HasIntegerAttribute<A, E, B>
	>
	void test(M meta, A name) {
		IntegerAttribute<A, E, B> ik1 = IntegerAttribute.get(meta, name);
		assertNotNull(ik1);
		assertSame(IntegerType.TYPE, ik1.type());
		IntegerAttribute<A, E, B> ik2 = IntegerAttribute.get(meta, name);
		assertSame(ik1, ik2);				
	}	
}
