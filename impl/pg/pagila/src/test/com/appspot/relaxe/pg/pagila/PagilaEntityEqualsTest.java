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

import com.appspot.relaxe.gen.pg.pagila.ent.pub.FilmActor;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Actor;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Film;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Language;
import com.appspot.relaxe.value.IntegerHolder;

public class PagilaEntityEqualsTest
	extends AbstractPagilaEntityTestCase {
		
	public void testEquals1a() {		
		Film.Mutable fm1 = newFilm();
		Film.Mutable fm2 = newFilm();
		
		assertFalse(fm1.equals(fm2));
		
		assertFalse(fm1.isIdentified());
		assertFalse(fm2.isIdentified());
		
		assertTrue(fm1.equals(fm1));			
		assertFalse(fm1.equals(null));
	}
	
	
	public void testEquals1b() {		
		Film.Mutable fm1 = newFilm();
		Film.Mutable fm2 = newFilm();
		
		fm1.setFilmId(IntegerHolder.NULL_HOLDER);				
		assertFalse(fm1.isIdentified());
		assertFalse(fm1.equals(fm2));
				
		fm2.setFilmId(IntegerHolder.NULL_HOLDER);		
		assertFalse(fm1.equals(fm2));
				
		fm1.setFilmId(IntegerHolder.valueOf(null));
		assertFalse(fm1.isIdentified());
		assertFalse(fm1.equals(fm2));
	}

	public void testEquals2() {		
		Film.Mutable fm1 = newFilm();
		Film.Mutable fm2 = newFilm();
		
		assertNull(fm1.toPrimaryKey());
				
		fm1.setFilmId(Integer.valueOf(1));
		
		assertTrue(fm1.isIdentified());
		
		assertFalse(fm1.equals(null));
		assertFalse(fm1.equals(fm2));
		
		assertTrue(fm1.equals(fm1));
				
		Film pk = fm1.toPrimaryKey();
		
		assertNotNull(pk);
		assertTrue(pk.isIdentified());
		
		assertTrue(fm1.equals(pk));
				
		assertTrue(pk.equals(pk));
		assertFalse(pk.equals(fm2));
		assertFalse(pk.equals(null));
		assertTrue(pk.equals(fm1));
		
		assertEquals(fm1.hashCode(), pk.hashCode());
		
		fm2.setFilmId(Integer.valueOf(1));
				
		assertTrue(fm1.equals(fm2));
		assertTrue(pk.equals(fm2));
		
		assertEquals(fm1.hashCode(), fm2.hashCode());		
	}
	
	
	public void testEquals3() {		
		Film.Mutable fm1 = newFilm();
		Film.Mutable fm2 = newFilm();
						
		fm1.setFilmId(Integer.valueOf(2));
		fm2.setFilmId(new Integer(2));
				
		assertTrue(fm1.equals(fm2));
		
		fm1.setLanguage(Language.Holder.NULL);
		assertFalse(fm1.equals(fm2));
		
		fm2.setLanguage(Language.Holder.NULL);
		assertTrue(fm1.equals(fm2));
		
		Language.Mutable ml1 = newEntity(Language.Type.TYPE);
		
		fm1.setLanguage(ml1);
		
		assertFalse(fm1.equals(fm2));
		
		fm2.setLanguage((Language.Holder) null);
		assertFalse(fm1.equals(fm2));
		
		fm2.setLanguage(ml1);
		
		assertTrue(fm1.equals(fm2));						
		assertEquals(fm1.hashCode(), fm2.hashCode());
		
		Language.Mutable ml2 = newEntity(Language.Type.TYPE);
				
		fm2.setLanguage(ml2);
		assertFalse(fm1.equals(fm2));
						
		ml1.setLanguageId(1);
		ml2.setLanguageId(1);
		
		assertTrue(fm1.equals(fm2));
		
		ml1.setLanguageId(1);
		ml2.setLanguageId(2);
		
		assertFalse(fm1.equals(fm2));
		
		ml1.setLanguageId(1);
		ml1.setLastUpdate(new Date());
		ml2.setLanguageId(1);
		ml1.setLastUpdate(new Date());
		
		// only the identityEquals matters for references 
		assertTrue(fm1.equals(fm2));
				
		final Film pk1 = fm1.toPrimaryKey();
		
		// different content
		assertFalse(fm1.equals(pk1));
		assertFalse(pk1.equals(fm1));
		
		final Film fi = fm1.toImmutable();
		assertEquals(fi, fm1);
		assertEquals(fm1, fi);
		
		assertEquals(fm1.hashCode(), fi.hashCode());
				
		final Film pk2 = fi.toPrimaryKey();
		
		assertEquals(pk1, pk2);
		assertEquals(pk2, pk1);
		
		assertEquals(pk1.hashCode(), pk2.hashCode());		
	}
	
	public void testEquals4() {		
		FilmActor.Mutable fam1 = newEntity(FilmActor.Type.TYPE);
		FilmActor.Mutable fam2 = newEntity(FilmActor.Type.TYPE);
		
		assertFalse(fam1.equals(fam2));
		assertEquals(fam1, fam1);
		assertFalse(fam1.equals(null));
		assertFalse(fam1.equals(new Object()));
				
		Actor.Mutable am = newActor();
		
		fam1.setActor(am);
		assertFalse(fam1.equals(fam2));
		
		fam2.setActor(am);
		assertFalse(fam1.equals(fam2));
		
		am.setActorId(1);
		assertFalse(fam1.equals(fam2));
		
		final Film.Mutable fm1 = newFilm();
		
		fam1.setFilm(fm1);
		assertFalse(fam1.equals(fam2));
		
		fm1.setFilmId(1);
		assertFalse(fam1.equals(fam2));
				
		assertTrue(fam1.isIdentified());
		assertFalse(fam2.isIdentified());
		
		fam2.setFilm(fm1);
		
		assertTrue(fam2.isIdentified());
		
		assertEquals(fam1, fam2);
		assertEquals(fam1.hashCode(), fam2.hashCode());
		
		Film fi1 = fm1.toImmutable();
		fam1.setFilm(fi1);
		
		assertEquals(fam1, fam2);
		assertEquals(fam1.hashCode(), fam2.hashCode());
		
		final Film.Mutable fm2 = newFilm();
		
		fm2.setFilmId(fm1.getFilmId().value());
		fam2.setFilm(fm2);
					
		assertEquals(fam1, fam2);
		assertEquals(fam1.hashCode(), fam2.hashCode());
		
		FilmActor pk1 = fam1.toPrimaryKey();
				
		assertEquals(fam1, pk1);
		
		FilmActor pk2 = fam2.toPrimaryKey();
		
		assertEquals(pk1, pk2);
	}

}
