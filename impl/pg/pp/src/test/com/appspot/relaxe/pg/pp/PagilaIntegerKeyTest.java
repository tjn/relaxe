/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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
package com.appspot.relaxe.pg.pp;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.value.HasInteger;
import com.appspot.relaxe.ent.value.HasIntegerKey;
import com.appspot.relaxe.ent.value.IntegerKey;
import com.appspot.relaxe.gen.pg.pp.ent.pub.Film;
import com.appspot.relaxe.types.IntegerType;

import junit.framework.TestCase;

public class PagilaIntegerKeyTest extends TestCase {

	public void testEquals() {
//		LiteralCatalog litcat = LiteralCatalog.getInstance();
		test(Film.Type.TYPE.getMetaData(), Film.Attribute.FILM_ID);
	}

	private
	<
		A extends Attribute,
		E extends HasInteger<A, E>,
		M extends HasIntegerKey<A, E>
	>
	void test(M meta, A name) {
		IntegerKey<A, E> ik1 = IntegerKey.get(meta, name);
		assertNotNull(ik1);
		assertSame(IntegerType.TYPE, ik1.type());
		IntegerKey<A, E> ik2 = IntegerKey.get(meta, name);
		assertSame(ik1, ik2);
	}
}
