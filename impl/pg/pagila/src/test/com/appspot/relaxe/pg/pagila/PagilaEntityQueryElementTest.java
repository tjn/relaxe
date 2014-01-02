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
package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Film;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Language;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;

public class PagilaEntityQueryElementTest 
	extends AbstractPagilaTestCase {
	
	public void testAddAllAttributes() throws Exception {
		Film.MetaData meta = Film.Type.TYPE.getMetaData();
		assertFalse(meta.attributes().isEmpty());
		Film.QueryElement.Builder qb = new Film.QueryElement.Builder();				
		qb.addAllAttributes();
		
		Film.QueryElement qe = qb.newQueryElement();		
		assertFalse(qe.attributes().isEmpty());
		assertEquals(meta.attributes(), qe.attributes());
	}
	
	public void testQueryElements() throws Exception {				
		Film.QueryElement.Builder qb = new Film.QueryElement.Builder();
		
		Language.QueryElement.Builder lb = new Language.QueryElement.Builder();
		final Language.QueryElement le = lb.newQueryElement();
				
		qb.setQueryElement(Film.LANGUAGE, le);
		assertNotNull(qb.getQueryElement(Film.LANGUAGE));
		assertNull(qb.getQueryElement(Film.ORIGINAL_LANGUAGE));
		
		Language.QueryElement qe = qb.getQueryElement(Film.LANGUAGE);
		assertSame(le, qe);
		
		final EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?> rle = qb.getQueryElement(Film.LANGUAGE);
		assertNotNull(rle);		
		assertTrue(rle == le);
		
		final Language.QueryElement ole = lb.newQueryElement();
		
		qb.setQueryElement(Film.ORIGINAL_LANGUAGE, ole);
		EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?> r2 = qb.getQueryElement(Film.ORIGINAL_LANGUAGE);
		assertNotNull(r2);
		assertTrue(r2 == ole);
		
		Film.QueryElement fqe = qb.newQueryElement();
		
		EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?> s1 = fqe.getQueryElement(Film.LANGUAGE);
		assertNotNull(s1);
		assertTrue(s1 == rle);
		
		EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?> s2 = fqe.getQueryElement(Film.ORIGINAL_LANGUAGE);
		assertNotNull(s2);
		assertTrue(s2 == ole);
		
		
	}
}
