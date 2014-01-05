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
package com.appspot.relaxe.pg.pp;

import com.appspot.relaxe.SimpleUnificationContext;
import com.appspot.relaxe.ent.im.EntityIdentityMap;
import com.appspot.relaxe.gen.pg.pp.ent.pub.Film;
import com.appspot.relaxe.gen.pg.pp.ent.pub.Film.Attribute;
import com.appspot.relaxe.gen.pg.pp.ent.pub.Film.Holder;
import com.appspot.relaxe.gen.pg.pp.ent.pub.Film.MetaData;
import com.appspot.relaxe.gen.pg.pp.ent.pub.Film.Reference;
import com.appspot.relaxe.gen.pg.pp.ent.pub.Film.Type;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;


public class PagilaUnificationContextTest extends AbstractPagilaTestCase {

	public void testUnify() {
//		if (System.currentTimeMillis() > 0)
//			throw new RuntimeException();


		SimpleUnificationContext c = new SimpleUnificationContext();
		Integer FID = Integer.valueOf(1);

		Film.MetaData meta = Film.Type.TYPE.getMetaData();
		EntityIdentityMap<Attribute, Reference, Type, Film, Holder, MetaData> im = meta.getIdentityMap(c);
		EntityIdentityMap<Attribute, Reference, Type, Film, Holder, MetaData> im2 = meta.getIdentityMap(c);

		assertSame(im, im2);

		Film f1 = newEntity(Film.Type.TYPE);
		f1.getContent().setFilmId(FID);

		Film f2 = newEntity(Film.Type.TYPE);
		f2.getContent().setFilmId(FID);

		Film.Holder h1 = im.get(f1);
		assertNotNull(h1);
		assertNotNull(h1.value());
		assertSame(f1, h1.value());

		Film.Holder h2 = im.get(f2);
		assertNotNull(h1);
		assertNotNull(h1.value());

		assertSame(f1, h2.value());

		c.close();

		EntityIdentityMap<Attribute, Reference, Type, Film, Holder, MetaData> im3 = meta.getIdentityMap(c);
		assertNotSame(im2, im3);
		c.close();
	}

}
