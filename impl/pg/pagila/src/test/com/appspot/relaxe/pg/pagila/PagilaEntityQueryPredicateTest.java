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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.ent.query.EntityQueryPredicate;
import com.appspot.relaxe.ent.query.EntityQueryPredicates;
import com.appspot.relaxe.expr.op.Comparison;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Actor;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Film;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.FilmActor;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;
import com.appspot.relaxe.service.DataAccessSession;
import com.appspot.relaxe.service.EntitySession;
import com.appspot.relaxe.value.VarcharHolder;

public class PagilaEntityQueryPredicateTest 
	extends AbstractPagilaTestCase {
	
	public void testIn1() throws Exception {
		
		Film.Query fq = null;
		
		{
			Film.QueryElement.Builder qeb = new Film.QueryElement.Builder();				
			qeb.addAllAttributes();
			Film.QueryElement qe = qeb.newQueryElement();
			Film.Query.Builder qb = new Film.Query.Builder(qe);
			qb.asc(Film.TITLE);
			fq = qb.newQuery();
		}
		
		DataAccessSession das = newSession();
		EntitySession es = das.asEntitySession();
								
		List<Film> films = es.list(fq, new FetchOptions(20, 0));
		
		Set<Integer> filmIds = new TreeSet<Integer>();		
		ints(films, filmIds, Film.FILM_ID);
		
		
		FilmActor.Query.Builder qb = new FilmActor.Query.Builder(new FilmActor.QueryElement());			
		FilmActor.QueryElement re = qb.getRootElement();
				
		EntityQueryPredicate inp = EntityQueryPredicates.newIn(re, FilmActor.FILM, films);
		qb.addPredicate(inp);
		FilmActor.Query faq = qb.newQuery();
		
		List<FilmActor> fas = es.list(faq, null);
						
		assertNotNull(fas);
		assertFalse(fas.isEmpty());
				
		for (FilmActor fa : fas) {
			Film.Holder fh = fa.getFilm(FilmActor.FILM);
			assertNotNull(fh);			
			Film film = fh.value();
			Integer id = film.getFilmId().value();
			assertNotNull(id);			
			filmIds.contains(id);
		}
	}
	
	public void testIn2() throws Exception {
								
		List<Film> films = new ArrayList<Film>();
		
		range(Film.Type.TYPE, films, Film.FILM_ID, 100);
		
		Film.Query.Builder qb = new Film.Query.Builder(new Film.QueryElement());			
		Film.QueryElement re = qb.getRootElement();
		
		EntityQueryPredicate inp = EntityQueryPredicates.newIn(re, films);
		qb.addPredicate(inp);
		
		Film.Query faq = qb.newQuery();
		
		assertResultEquals(faq, Film.FILM_ID, "SELECT film_id FROM film WHERE film_id BETWEEN 1 AND 100", false);		
	}
	
	public void testIn3() throws Exception {
		
		FilmActor.QueryElement fae = new FilmActor.QueryElement();
		FilmActor.Query.Builder qb = new FilmActor.Query.Builder(fae);
		
		List<Film> fl = range(Film.Type.TYPE, Film.FILM_ID, 10);
		
		EntityQueryPredicate inp = EntityQueryPredicates.newIn(fae, FilmActor.FILM, fl);
		qb.addPredicate(inp);
		
		FilmActor.Query q = qb.newQuery();
		
		DataAccessSession das = newSession();		
		EntitySession es = das.asEntitySession();
				
		List<FilmActor> fas = es.list(q, null);
		
		Set<Integer> fids = new TreeSet<Integer>();
		Set<Integer> aids = new TreeSet<Integer>();
				
		for (FilmActor fa : fas) {
			fids.add(fa.getFilm(FilmActor.FILM).value().getFilmId().value());
			aids.add(fa.getActor(FilmActor.ACTOR).value().getActorId().value());
		}
				
		String faq = "SELECT actor_id, film_id FROM film_actor " +
				"WHERE film_id IN ( " +
				"SELECT film_id FROM film a WHERE a.film_id BETWEEN 1 AND 10) ";
		
		Set<Integer> qas = ints(faq, new TreeSet<Integer>(), 1);
		Set<Integer> qfs = ints(faq, new TreeSet<Integer>(), 2);
		
		assertEquals("actor set", qas, aids);
		assertEquals("film set", qfs, fids);		
						
	}
	
	public void testIn4() throws Exception {
		
		List<FilmActor> fas = getFilmActors(new ArrayList<FilmActor>(), 10, 150);
		
		Film.Query.Builder qb = new Film.Query.Builder(new Film.QueryElement());			
		Film.QueryElement re = qb.getRootElement();
		
		EntityQueryPredicate inp = EntityQueryPredicates.newInReferencing(re, FilmActor.FILM, fas);
		qb.addPredicate(inp);
		
		Film.Query q = qb.newQuery();
		
		assertResultEquals(q, Film.FILM_ID, 
				"SELECT film_id FROM film " +
				"WHERE film_id IN ( " +
						"SELECT film_id FROM film_actor a WHERE a.film_id BETWEEN 1 AND 10 AND a.actor_id BETWEEN 1 AND 150) ", 
				false);
	}

	public List<FilmActor> getFilmActors(List<FilmActor> fas, int fc, int ac) throws Exception {
		FilmActor.Type t = FilmActor.Type.TYPE;		
		FilmActor.Factory ef = t.getMetaData().getFactory();
		
		List<Film> fl = range(Film.Type.TYPE, Film.FILM_ID, fc);
		List<Actor> al = range(Actor.Type.TYPE, Actor.ACTOR_ID, ac);
						
		for (Film f : fl) {
			for (Actor a : al) {
				FilmActor.Mutable fa = ef.newEntity();
				fa.setFilm(FilmActor.FILM, f.ref());
				fa.setActor(FilmActor.ACTOR, a.ref());
				fas.add(fa);
			}
		}
		
		return fas;
	}	

	public void testLike1() throws Exception {
		Film.Query.Builder qb = new Film.Query.Builder(new Film.QueryElement(Film.FILM_ID, Film.TITLE));
		Film.QueryElement re = qb.getRootElement();		
		EntityQueryPredicate tp = re.newPredicate(Film.TITLE, Comparison.Op.LIKE, VarcharHolder.valueOf("T%"));				
		qb.addPredicate(tp);				
		List<Film> results = assertSetEquals(qb.newQuery(), Film.FILM_ID, "SELECT film_id FROM film WHERE title LIKE 'T%'");		
		assertFalse("No test data available", results.isEmpty());		
	}
	
	public void testLike2() throws Exception {
		Film.Query.Builder qb = new Film.Query.Builder(new Film.QueryElement());
		Film.QueryElement re = qb.getRootElement();		
		EntityQueryPredicate tp = re.newPredicate(Film.TITLE, Comparison.Op.LIKE, VarcharHolder.valueOf("%TAPE%"));				
		qb.addPredicate(tp);				
		List<Film> results = assertSetEquals(qb.newQuery(), Film.FILM_ID, "SELECT film_id FROM film WHERE title LIKE '%TAPE%'");
		assertFalse("No test data available", results.isEmpty());
	}
	
	public void testLike3() throws Exception {
		Film.Query.Builder qb = new Film.Query.Builder(new Film.QueryElement());
		Film.QueryElement re = qb.getRootElement();		
		EntityQueryPredicate tp = re.newPredicate(Film.TITLE, Comparison.Op.LIKE, VarcharHolder.valueOf("ANNIE IDENTITY"));				
		qb.addPredicate(tp);				
		List<Film> results = assertSetEquals(qb.newQuery(), Film.FILM_ID, "SELECT film_id FROM film WHERE title LIKE 'ANNIE IDENTITY'");
		assertFalse("No test data available", results.isEmpty());
	}	
	
	

}
