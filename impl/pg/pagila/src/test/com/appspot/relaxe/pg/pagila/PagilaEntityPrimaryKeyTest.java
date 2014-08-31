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

import com.appspot.relaxe.gen.pg.pagila.ent.pub.Actor;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Film;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.FilmActor;
import com.appspot.relaxe.value.IntegerHolder;

public class PagilaEntityPrimaryKeyTest
	extends AbstractPagilaEntityTestCase {
		
	public void testNullPK1() {		
		Film.Mutable fo = newFilm();		
		Film pk = fo.toPrimaryKey();
		assertNull(pk);
		
		Film fe = fo.as();				
		assertNotNull(fe);
		
		pk = fe.toPrimaryKey();
		assertNull(pk);
	}
	
	public void testNullPK2() {		
		Film.Mutable fo = newFilm();
		fo.setFilmId((Integer) null);
		
		{
			Film pk = fo.toPrimaryKey();
			assertNull(pk);
		}
		
		{
			Film fe = fo.as();
			assertNotNull(fe);
			Film pk = fe.toPrimaryKey();
			assertNull(pk);
		}
		
		fo.setFilmId(IntegerHolder.NULL_HOLDER);

		{
			Film pk = fo.toPrimaryKey();
			assertNull(pk);
		}
		
		{
			Film fe = fo.as();
			assertNotNull(fe);
			Film pk = fe.toPrimaryKey();
			assertNull(pk);
		}	
	}
	
	
	public void testPK1() {
		Film.Mutable fo = newFilm();		
		fo.setFilmId(Integer.valueOf(13));
		
		{
			Film pk = fo.toPrimaryKey();		
			testFilmPrimaryKey(pk, 13);
		}
		
		fo.setTitle("FF");
		
		{
			Film pk = fo.toPrimaryKey();		
			testFilmPrimaryKey(pk, 13);
		}
		
		fo.setFilmId(Integer.valueOf(14));
		
		{
			Film pk = fo.toPrimaryKey();		
			testFilmPrimaryKey(pk, 14);
		}	
	}
	
	
	
	public void testNullPK3() {		
		FilmActor.Mutable fam = newEntity(FilmActor.Type.TYPE);
				
		{
			FilmActor pk = fam.toPrimaryKey();
			assertNull(pk);
		}
		
		{
			Film.Mutable fm = newFilm();
			fam.setFilm(FilmActor.FILM, fm.as().ref());			
			assertNull(fam.toPrimaryKey());			
		}

		{
			Actor.Mutable am = newActor();
			fam.setActor(FilmActor.ACTOR, am.as().ref());			
			assertNull(fam.toPrimaryKey());			
		}
		
		Film.Mutable fm = fam.getFilm(FilmActor.FILM).value().asMutable();
		assertNotNull(fm);
		
		Actor.Mutable am = fam.getActor(FilmActor.ACTOR).value().asMutable();
		assertNotNull(am);
		
		{
			fm.setFilmId(Integer.valueOf(13));						
			assertNull(fam.toPrimaryKey());			
		}
		
		{
			am.setActorId(Integer.valueOf(14));
			FilmActor fak = fam.toPrimaryKey();			
			testFilmActorPrimaryKey(fak, 13, 14);
		}		
	}

	public void testNullPK4() {		
		FilmActor.Mutable fam = newEntity(FilmActor.Type.TYPE);
		Film.Mutable fm = newEntity(Film.Type.TYPE);
		fam.setFilm(FilmActor.FILM, fm.as().ref());			

		Actor.Mutable am = newActor();
		fam.setActor(FilmActor.ACTOR, am.as().ref());			
		
		fm.setFilmId(Integer.valueOf(13));						
		am.setActorId(Integer.valueOf(14));
		
		
		{
			FilmActor fak = fam.toPrimaryKey();
			testFilmActorPrimaryKey(fak, 13, 14);
		}
		
		{
			am.setActorId(Integer.valueOf(15));		
			FilmActor fak = fam.toPrimaryKey();	
			testFilmActorPrimaryKey(fak, 13, 15);
		}
		
		{
			am.setActorId(IntegerHolder.NULL_HOLDER);		
			FilmActor fak = fam.toPrimaryKey();	
			assertNull(fak);			
		}
		
		{
			am.setActorId((IntegerHolder) null);		
			FilmActor fak = fam.toPrimaryKey();	
			assertNull(fak);			
		}
		
	}
	


}
