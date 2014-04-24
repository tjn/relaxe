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

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.appspot.relaxe.ent.EntityQueryResult;
import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Actor;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Film;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Film.Attribute;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.FilmActor;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.FilmActor.Factory;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.FilmActor.Holder;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.FilmActor.MetaData;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.FilmActor.Query;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.FilmActor.QueryElement;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.FilmActor.Reference;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.FilmActor.Type;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Language;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.paging.ResultPage;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;
import com.appspot.relaxe.service.DataAccessSession;
import com.appspot.relaxe.service.EntitySession;

public class PagilaDataAccessSessionTest 
	extends AbstractPagilaTestCase {

	public void testLoad() throws Exception {		
		logger().debug("testLoad - enter");
		
		DataAccessSession das = newSession();
		
		assertNotNull(das);
		
		EntitySession es = das.asEntitySession();
		assertNotNull(es);	
						
		Film.QueryElement.Builder fq = new Film.QueryElement.Builder();
		fq.addAllAttributes();
		
		Language.QueryElement.Builder pt = new Language.QueryElement.Builder();
		pt.addAllAttributes();
		
		Language.QueryElement le = pt.newQueryElement();
		fq.setQueryElement(Film.LANGUAGE, le);

		fq.setQueryElement(Film.ORIGINAL_LANGUAGE, le);
		
		Film.Query qo = new Film.Query(fq.newQueryElement());
				
		FetchOptions fo = new FetchOptions(20, 0);
		List<Film> load = es.list(qo, fo);
		
		logger().debug("testLoad again: result set size: " + load.size());
		es.list(qo, fo);

		
		PagilaInspector inspector = new PagilaInspector();						
		inspector.inspect(load);
		
		logger().debug("testLoad: result set size: " + load.size());
		logger().debug("testLoad: stats:\n\n" + inspector);
				
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(data);
		
		oos.writeObject(load);
		oos.close();
				
		das.close();
		
		logger().debug("testLoad: data.size()=" + data.size());
		logger().debug("testLoad: inspector.getInstanceCount() : " + inspector.getInstanceCount());
		logger().debug("testLoad: inspector.getReferenceCount(): " + inspector.getReferenceCount());
		logger().debug("testLoad - exit");
	}



	public void testLoad2() throws Exception {
		
		DataAccessSession das = newSession();		
		EntitySession es = das.asEntitySession();
				
		Film.QueryElement.Builder qeb = new Film.QueryElement.Builder();
		
		qeb.add(Film.TITLE);
		qeb.add(Film.DESCRIPTION);
								
		Film.MetaData meta = Film.Type.TYPE.getMetaData();
		
		for (Film.Attribute a : meta.attributes()) {
			Column col = meta.getColumn(a);
			assertNotNull(col);
			Attribute b = meta.getAttribute(col);
			assertSame(a, b);
		}		
		
		{
			Film.Query qo = new Film.Query(qeb.newQueryElement());
			
			List<Film> list = es.list(qo, null);
				
			assertNotNull(list);
			assertTrue(list.size() > 10);			
			
			for (Film film : list) {
				assertNotNull(film);
				// ID by default builder
				assertTrue(film.attributes().contains(Film.FILM_ID.name()));
				assertTrue(film.attributes().contains(Film.TITLE.name()));
				assertTrue(film.attributes().contains(Film.DESCRIPTION.name()));			
			}
		}
		
		{
			qeb.remove(Film.Attribute.FILM_ID);
			
//			Film.Query qo = new Film.Query(qeb.newQueryElement());
//			
//			List<Film> list = es.load(qo, null);
//				
//			assertNotNull(list);
//			assertTrue(list.size() > 10);			
//			
//			for (Film film : list) {
//				assertNotNull(film);
//				// ID removed
//				assertFalse(film.attributes().contains(Film.FILM_ID.name()));
//				assertTrue(film.attributes().contains(Film.TITLE.name()));
//				assertTrue(film.attributes().contains(Film.DESCRIPTION.name()));			
//			}
		}
	}

	
	public void testLoadAllAttributes() throws Exception {
		
		DataAccessSession das = newSession();
		
		EntitySession es = das.asEntitySession();
		
		Film.QueryElement.Builder qeb = new Film.QueryElement.Builder();
		qeb.addAllAttributes();
								
		Film.MetaData meta = Film.Type.TYPE.getMetaData();
		
		
		Film.Query q = new Film.Query(qeb.newQueryElement());
						
		List<Film> list = es.list(q, null);
		
		assertNotNull(list);
		assertTrue(list.size() > 10);			
		
		for (Film film : list) {			
			assertNotNull(film);
			logger().debug("film: " + film.getFilmId());
			logger().debug("film.attributes(): " + film.attributes());
			logger().debug("meta.attributes(): " + meta.attributes());
			
			assertEquals(meta.attributes(), film.attributes());
		}
	}
	
	
	public void testQuery() throws Exception {
		
		logger().debug("testQuery");

		DataAccessSession das = newSession();		
		
		EntitySession es = das.asEntitySession();
		
		FilmActor.QueryElement.Builder eb = new FilmActor.QueryElement.Builder();
		eb.addAllAttributes();
		
		Film.QueryElement.Builder le = new Film.QueryElement.Builder();
		le.add(Film.FILM_ID);
		le.add(Film.TITLE);
		le.add(Film.LAST_UPDATE);
		
		le.setQueryElement(Film.LANGUAGE, new Language.QueryElement(Language.LANGUAGE_ID, Language.LAST_UPDATE));
		
		Actor.QueryElement ae = new Actor.QueryElement();
					
		eb.setQueryElement(FilmActor.ACTOR, ae);
		eb.setQueryElement(FilmActor.FILM, le.newQueryElement());
						
		
		FilmActor.QueryElement qe = eb.newQueryElement();
		FilmActor.Query.Builder qb = new FilmActor.Query.Builder(qe);
		qb.addPredicate(ae.newEquals(Actor.ACTOR_ID, new Integer(1)));
		
		ResultPage rp = es.query(qb.newQuery(), null);
		assertNotNull(rp);			
		
	}
		
}
