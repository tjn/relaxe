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

import com.appspot.relaxe.gen.pg.pagila.ent.pub.Film;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Language;
import com.appspot.relaxe.value.IntegerHolder;

public class PagilaEntityAttributeTest
	extends AbstractPagilaEntityTestCase {
		
	public void testAttributes() {		
		Film.Mutable fo = newFilm();
		
		{
			assertNull(fo.getFilmId());
			Integer value = Integer.valueOf(13);		
			fo.setFilmId(value);
			assertNotNull(fo.getFilmId());
			assertNotNull(fo.getFilmId().value());
			assertEquals(value, fo.getFilmId().value());
			
			IntegerHolder h = fo.getInteger(Film.FILM_ID);
			assertNotNull(h);
			assertEquals(value, h.value());
		}
		
		{
			Integer value = Integer.valueOf(5);
			fo.setInteger(Film.FILM_ID, IntegerHolder.valueOf(value));
			assertEquals(value, fo.getFilmId().value());
		}
		
		{			
			fo.setInteger(Film.FILM_ID, IntegerHolder.valueOf(null));
			assertNotNull(fo.getFilmId());
			assertTrue(fo.getFilmId().isNull());
			assertTrue(fo.getFilmId().value() == null);
		}
		
		{			
			fo.setInteger(Film.FILM_ID, IntegerHolder.NULL_HOLDER);
			assertNotNull(fo.getFilmId());
			assertTrue(fo.getFilmId().isNull());
			assertTrue(fo.getFilmId().value() == null);
		}
		
		{
			fo.setFilmId((Integer) null);
			assertNotNull(fo.getInteger(Film.FILM_ID));
			assertNull(fo.getInteger(Film.FILM_ID).value());
		}
		
		{
			fo.setFilmId((IntegerHolder) null);
			assertNull(fo.getInteger(Film.FILM_ID));			
		}
	}
	
	public void testReferences() {
		Film.Mutable fo = newEntity(Film.Type.TYPE);
		
		{
			assertNull(fo.getLanguage(Film.LANGUAGE));
			
			fo.setLanguage((Language) null);
			Language.Holder h = fo.getLanguage();		
			assertNotNull(h);
			assertTrue(h.isNull());
			
			assertSame(h, fo.getLanguage(Film.LANGUAGE));
			
		}
		
		{
			Language lang = newEntity(Language.Type.TYPE);
			fo.setLanguage(lang);
			Language.Holder h = fo.getLanguage();
			assertNotNull(h);
			assertFalse(h.isNull());
		}
		
		{
			Language lang = newEntity(Language.Type.TYPE);
			fo.setLanguage(lang);
			Language.Holder h = fo.getLanguage();
			assertNotNull(h);
			assertFalse(h.isNull());
			
			assertSame(h, fo.getLanguage(Film.LANGUAGE));			
		}	
		
				
	}
}
