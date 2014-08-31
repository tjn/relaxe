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

import java.util.Date;

import com.appspot.relaxe.ent.Operation;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Film;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Language;

public class PagilaEntityToImmutableTest
	extends AbstractPagilaEntityTestCase {
		
	public void testEmpty() {

		Film.Mutable fm = newFilm();		
		Film film = fm.toImmutable();
		testImmutable(film);		
		assertFalse(film.isIdentified());		
		film.isMutable();
		
		
	}
	
	public void testEmpty1() {
		Film.Mutable fm = newFilm();		
		Film film = fm.toImmutable();
		assertNotNull(film);
		assertNull(film.toPrimaryKey());		
		
	}
		
	public void testPrimaryKey() {
		Film.Mutable fm = newFilm();
		
		Film film1 = null;
		Film film2 = null;
		
		{
			Integer v = Integer.valueOf(3);
			fm.setFilmId(v);
						
			testFilmPrimaryKey(fm.toPrimaryKey(), v.intValue());
			
			film1 = fm.toImmutable();		
			assertNotNull(film1);
			testImmutable(film1);
			assertEquals(v, film1.getFilmId().value());
			
			Film pk = film1.toPrimaryKey();
			testFilmPrimaryKey(pk, v.intValue());
			
			Film pk2 = film1.toPrimaryKey();
			assertSame(pk, pk2);
		}
		
		{
			final String title = "-";
			final Date upd = new Date();
			
			Integer v = Integer.valueOf(4);
			fm.setFilmId(v);
			fm.setLastUpdate(upd);
			fm.setTitle(title);
			
			testFilmPrimaryKey(fm.toPrimaryKey(), v.intValue());
			
			film2 = fm.toImmutable();		
			assertNotNull(film2);
			testImmutable(film2);
			assertEquals(v, film2.getFilmId().value());
			
			assertEquals(title, film2.getTitle().value());
			assertEquals(upd, film2.getLastUpdate().value());
			
			
			Film pk = film2.toPrimaryKey();
			testFilmPrimaryKey(pk, v.intValue());
			
			Film pk2 = film2.toPrimaryKey();
			assertSame(pk, pk2);
		}		
		
		assertNotSame(film1,  film2);
	}
	
	
	public void testNullRefererences() {
		Film.Mutable fm = newFilm();
		
		fm.setLanguage(Language.Holder.NULL);
		testLanguageNullReference(fm);
		
		Film film = null;
		
		film = fm.toImmutable();
		assertNotNull(film);
		testImmutable(film);
		testLanguageNullReference(film);
		
		{
			Language.Holder h1 = film.getOriginalLanguage();
			assertNull(h1);			
			
			Language.Holder h2 = film.getLanguage(Film.ORIGINAL_LANGUAGE);
			assertNull(h2);
		}
	}	
	
	public void testRefererences1() {
		Film.Mutable fm = newFilm();
				
		Language.Mutable ml = newEntity(Language.Type.TYPE);
		ml.setName("English");

		final Language en = ml.toImmutable();
		
		ml.setName("French");
		final Language fr = ml.toImmutable();
		
		assertNotSame(en, fr);
						
		fm.setLanguage(en);
		fm.setOriginalLanguage(fr);
						
		Film film = fm.toImmutable();
		
		assertNotNull(film);
		testImmutable(film);
				
		assertSame(en, film.getLanguage().value());
		assertSame(en, film.getLanguage(Film.LANGUAGE).value());
		
		assertSame(fr, film.getOriginalLanguage().value());
		assertSame(fr, film.getLanguage(Film.ORIGINAL_LANGUAGE).value());
		
	}
	
	
	public void testRefererences2() {
		Film.Mutable fm = newFilm();
				
		Language.Mutable ml = newEntity(Language.Type.TYPE);
		ml.setName("English");

		final Language en = ml.toImmutable();
								
		fm.setLanguage(en);
		fm.setOriginalLanguage(en);
						
		Film film = fm.toImmutable();
		
		assertNotNull(film);
		testImmutable(film);
				
		assertSame(en, film.getLanguage().value());
		assertSame(en, film.getLanguage(Film.LANGUAGE).value());
		
		assertSame(en, film.getOriginalLanguage().value());
		assertSame(en, film.getLanguage(Film.ORIGINAL_LANGUAGE).value());
	}	
	
	
	public void testRefererences3() {
		Film.Mutable fm = newFilm();
				
		Language.Mutable ml = newEntity(Language.Type.TYPE);		
		ml.setName("English");
										
		fm.setLanguage(ml);
		fm.setOriginalLanguage(ml);
						
		Film film = fm.toImmutable();
		
		assertNotNull(film);
		testImmutable(film);
		
		final Language en = film.getLanguage().value();
		assertNotNull(en);
		testImmutable(en);
				
		assertSame(en, film.getLanguage(Film.LANGUAGE).value());		
		assertSame(en, film.getOriginalLanguage().value());
		assertSame(en, film.getLanguage(Film.ORIGINAL_LANGUAGE).value());
	}
	
	public void testRefererences4() {
		Film.Mutable fm = newFilm();
				
		Language.Mutable ml = newEntity(Language.Type.TYPE);						
		ml.setName("English");								
		
		fm.setLanguage(ml);
		
		Operation op = new Operation();						
		Film film1 = fm.toImmutable(op.getContext());
		op.finish();
		
		assertNotNull(film1);
		testImmutable(film1);
		
		final Language en = film1.getLanguage().value();
		assertNotNull(en);
		testImmutable(en);
	
		testNullOriginalLanguage(film1);
				
		fm.setOriginalLanguage(ml);
		testNotNullOriginalLanguage(fm);
		Film film2 = fm.toImmutable(op.getContext());
		op.finish();
		
		testNotNullOriginalLanguage(film2);
		
		
				
	}
	
	

	protected void testNullLanguage(Film film) {
		Language.Holder h1 = film.getLanguage();
		assertNull(h1);
				
		Language.Holder h2 = film.getLanguage(Film.LANGUAGE);
		assertNull(h2);
	}
	
	protected void testNullOriginalLanguage(Film film) {
		Language.Holder h1 = film.getOriginalLanguage();
		assertNull(h1);
				
		Language.Holder h2 = film.getLanguage(Film.ORIGINAL_LANGUAGE);
		assertNull(h2);
	}
	
	protected void testNotNullLanguage(Film film) {
		Language.Holder h1 = film.getLanguage();
		assertNotNull(h1);
		assertFalse(h1.isNull());
		
		Language.Holder h2 = film.getLanguage(Film.LANGUAGE);
		assertSame(h1, h2);
	}
	
	protected void testNotNullOriginalLanguage(Film film) {
		Language.Holder h1 = film.getOriginalLanguage();
		assertNotNull(h1);
		assertFalse(h1.isNull());
		
		Language.Holder h2 = film.getLanguage(Film.ORIGINAL_LANGUAGE);
		assertSame(h1, h2);
	}
	
	protected void testLanguageNullReference(Film film) {
		Language.Holder h1 = film.getLanguage();
		assertNotNull(h1);
		assertNotNull(h1.isNull());
		
		Language.Holder h2 = film.getLanguage(Film.LANGUAGE);
		assertSame(h1, h2);
	}
	
	protected void testOriginalLanguageNullReference(Film film) {
		Language.Holder h1 = film.getOriginalLanguage();
		assertNotNull(h1);
		assertNotNull(h1.isNull());
		
		Language.Holder h2 = film.getLanguage(Film.ORIGINAL_LANGUAGE);
		assertSame(h1, h2);
	}		

}
