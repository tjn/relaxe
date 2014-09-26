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

import java.util.List;

import com.appspot.relaxe.EntityQueryExpressionBuilder;
import com.appspot.relaxe.SimpleUnificationContext;
import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityDataObject;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQuery;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.ent.query.EntityQueryExistsPredicate;
import com.appspot.relaxe.ent.query.EntityQueryPredicate;
import com.appspot.relaxe.ent.query.EntityQueryPredicates;
import com.appspot.relaxe.ent.query.EntityQueryValue;
import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Actor;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Film;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Film.Query;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.FilmActor;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Language;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;
import com.appspot.relaxe.query.QueryResult;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.IntegerHolder;
import com.appspot.relaxe.value.ReferenceHolder;

public class PagilaEntityQueryExecutorTest 
	extends AbstractPagilaTestCase {

//	private QueryResult<EntityDataObject<Film>> execute(Film.QueryElement root) throws Exception {
//		return execute(root, null);		
//	}
	
	public 
	<
		A extends AttributeName,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>,
		RE extends EntityQueryElement<A, R, T, E, B, H, F, M, RE>
	>
	QueryResult<EntityDataObject<E>> execute(EntityQuery<A, R, T, E, B, H, F, M, RE> query, FetchOptions opts) throws Exception {	
		return super.execute(query, opts, getPersistenceContext(), getIdentityContext());
	}
	
	private QueryResult<EntityDataObject<Film>> execute(Film.QueryElement root, FetchOptions opts) throws Exception {				
		return execute(new Film.Query(root), opts, getPersistenceContext(), getIdentityContext());		
	}

//	public 
//	<
//		A extends AttributeName,
//		R extends Reference,
//		T extends ReferenceType<A, R, T, E, B, H, F, M>,
//		E extends Entity<A, R, T, E, B, H, F, M>,
//		B extends MutableEntity<A, R, T, E, B, H, F, M>,
//		H extends ReferenceHolder<A, R, T, E, H, M>,
//		F extends EntityFactory<E, B, H, M, F>,
//		M extends EntityMetaData<A, R, T, E, B, H, F, M>,
//		RE extends EntityQueryElement<A, R, T, E, B, H, F, M, RE>
//	>
//	QueryResult<EntityDataObject<E>> execute(EntityQuery<A, R, T, E, B, H, F, M, RE> query, FetchOptions opts, PersistenceContext<?> pc) throws Exception {
//		Connection c = newConnection();
//				
//		try {
//			M qm = query.getRootElement().getMetaData();
//			EntityQueryExecutor<A, R, T, E, B, H, F, M, RE> qe = createExecutor(qm, pc);
//			
//			
//			EntityQueryResult<A, R, T, E, B, H, F, M, RE> er = qe.execute(query, opts, c);
//			assertNotNull(er);
//			
//			DataObjectQueryResult<EntityDataObject<E>> qr = er.getContent(); 
//			assertNotNull(qr);
//			
//			DataObject.MetaData meta = qr.getMeta();
//			assertNotNull(meta);
//			
//			int cc = meta.getColumnCount();
//			assertTrue(cc > 0);
//			
//			for (int i = 0; i < cc; i++) {
//				ValueExpression ve = meta.expr(i);
//				assertNotNull(ve);
//			}
//			
//			
//			return qr;			
//		}
//		finally {
//			if (c != null) {
//				c.close();
//			}
//		}
//	}	
//	
//	
//
////	private Connection newConnection(Implementation imp) throws Exception {
////		Properties cfg = new Properties();
////		cfg.setProperty("user", "test");
////		cfg.setProperty("password", "test");
////		
////		String url = imp.createJdbcUrl("relaxe_test");
////		Class.forName(imp.defaultDriverClassName());
////		Connection c = DriverManager.getConnection(url, cfg);
////		return c;
////	}
//		
//
//	public <
//		A extends com.appspot.relaxe.ent.AttributeName,
//		R extends com.appspot.relaxe.ent.Reference,
//		T extends ReferenceType<A, R, T, E, B, H, F, M>,
//		E extends Entity<A, R, T, E, B, H, F, M>,
//		B extends MutableEntity<A, R, T, E, B, H, F, M>,
//		H extends ReferenceHolder<A, R, T, E, H, M>,
//		F extends EntityFactory<E, B, H, M, F>,		
//		M extends EntityMetaData<A, R, T, E, B, H, F, M>,
//		QE extends EntityQueryElement<A, R, T, E, B, H, F, M, QE>
//	>
//	EntityQueryExecutor<A, R, T, E, B, H, F, M, QE> createExecutor(M meta, PersistenceContext<?> persistenceContext) {
//		return new EntityQueryExecutor<A, R, T, E, B, H, F, M, QE>(persistenceContext, getIdentityContext());
//	}
//	
	
	public void testExecute3() throws Exception {		
		Film.QueryElement.Builder hrq = new Film.QueryElement.Builder(); 
		hrq.addAllAttributes();
		
		Language.QueryElement.Builder lq = new Language.QueryElement.Builder();
		lq.addAllAttributes();
		
		hrq.setQueryElement(Film.LANGUAGE, lq.newQueryElement());
				
		Film.QueryElement qe = hrq.newQueryElement();
				
		QueryResult<EntityDataObject<Film>> qr = execute(qe, null);
		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<Film>> el = qr.getContent();
		assertNotNull(el);
		
		for (EntityDataObject<Film> eo : el) {
			assertNotNull(eo);
			Film hr = eo.getRoot();
			assertNotNull(hr);
			
			Language.Holder lh = hr.getLanguage(Film.LANGUAGE);
			assertNotNull(lh);
			
			Language lang = lh.value();
			assertNotNull(lang);
			assertTrue(lang.isIdentified());
		}
	}

	public void testExecute4() throws Exception {
		Film.QueryElement.Builder hrq = new Film.QueryElement.Builder(); 
		hrq.addAllAttributes();
				
		Language.QueryElement.Builder oq = new Language.QueryElement.Builder();		
		
		Language.QueryElement le = oq.newQueryElement();		
		hrq.setQueryElement(Film.LANGUAGE, le);
		
		Film.Query.Builder qb = new Film.Query.Builder(hrq.newQueryElement());
		Film.Query qo = qb.newQuery();
				
		QueryResult<EntityDataObject<Film>> qr = execute(qo, null);
		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<Film>> el = qr.getContent();
		assertNotNull(el);
		
		for (EntityDataObject<Film> eo : el) {
			assertNotNull(eo);
			Film hr = eo.getRoot();
			assertNotNull(hr);
			
			Language.Holder oh = hr.getLanguage(Film.LANGUAGE);
			assertNotNull(oh);
			Language lang = oh.value();
			assertNotNull(lang);
			assertTrue(lang.isIdentified());
		}
	}
	
	public void testExecute5() throws Exception {
		Film.QueryElement.Builder hrq = new Film.QueryElement.Builder(); 
		hrq.addAllAttributes();
							
		hrq.setQueryElement(Film.LANGUAGE, (Language.QueryElement) null);
				
		QueryResult<EntityDataObject<Film>> qr = execute(hrq.newQueryElement(), null);
		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<Film>> el = qr.getContent();
		assertNotNull(el);
		
		for (EntityDataObject<Film> eo : el) {
			assertNotNull(eo);
			Film hr = eo.getRoot();
			assertNotNull(hr);
			
			Language.Holder lh = hr.getLanguage(Film.LANGUAGE);
			logger().debug("lh: " + lh);
			assertNull(lh);
			
			if (lh != null) {
				logger().debug("lh.isNull: " + lh.isNull());
				logger().debug("lh.value(): " + lh.value());
			}
		}
	}
	
	public void testExecute6() throws Exception {
		Film.QueryElement.Builder hrq = new Film.QueryElement.Builder(); 
		hrq.addAllAttributes();
		
		Language.QueryElement.Builder jt = new Language.QueryElement.Builder();
		jt.addAllAttributes();		
		hrq.setQueryElement(Film.ORIGINAL_LANGUAGE, jt.newQueryElement());
						
		
		Language.QueryElement.Builder ot = new Language.QueryElement.Builder();
			
		ot.removeAll(Language.Type.TYPE.getMetaData().attributes());
		
		hrq.setQueryElement(Film.LANGUAGE, ot.newQueryElement());
		
//		Person.QueryElement.Builder pt = new Person.QueryElement.Builder();
//		pt.add(Person.Attribute.DATE_OF_BIRTH, Person.Attribute.LAST_NAME);
//		ot.setTemplate(Language.FK_COMPANY_CEO, pt);
//		
//		QueryResult<EntityDataObject<Film>> qr = execute(hrq);
//		
//		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
//		
//		List<? extends EntityDataObject<Film>> el = qr.getContent();
//		assertNotNull(el);
//		
//		for (EntityDataObject<Film> eo : el) {
//			assertNotNull(eo);
//			Film hr = eo.getRoot();
//			assertNotNull(hr);
//			
//			IntervalHolder.DayTime dt = hr.getInterval(Film.TRAVEL_TIME);
//			assertNotNull(dt);
//			
//			Language.Holder jh = hr.getLanguage(Film.ORIGINAL_LANGUAGE);
//			assertNotNull(jh);
//			assertNotNull(jh.value());			
//			
//			Language.Holder oh = hr.getLanguage(Film.LANGUAGE);
//			assertNotNull(oh);
//			Language org = oh.value();
//			assertNotNull(org);
//			assertTrue(org.isIdentified());
//			Person.Holder ph = org.getPerson(Language.FK_COMPANY_CEO);
//			assertNotNull(ph);
//			
//			Person p = ph.value();
//			
//			if (p != null) {
//				Set<Person.Attribute> as = p.attributes();
//				assertEquals(3, as.size());
//				assertTrue(as.contains(Person.Attribute.ID));
//				assertTrue(as.contains(Person.Attribute.DATE_OF_BIRTH));
//				assertTrue(as.contains(Person.Attribute.LAST_NAME));
//				
//			}
//		}
	}
	
	public void testExecuteLimits() throws Exception {
		Film.QueryElement.Builder hrq = new Film.QueryElement.Builder();
				
		hrq.addAllAttributes();
		
		assertFalse(hrq.newQueryElement().attributes().isEmpty());
				
			
		Film.QueryElement q3 = hrq.newQueryElement();								
		
		QueryResult<EntityDataObject<Film>> qr = execute(q3, new FetchOptions(3, 3));		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<Film>> el = qr.getContent();
		assertNotNull(el);
		
		logger().debug("testExecuteQuery: size=" + el.size());		
		assertTrue(el.size() <= 3);
		
		Film.QueryElement q36 = hrq.newQueryElement();
		
		qr = execute(q36, new FetchOptions(3, 6));
		
		Long a = qr.getAvailable();
		assertNotNull(a);
		assertFalse(a.longValue() == 3);
		
		el = qr.getContent();
		assertNotNull(el);
		
		logger().debug("testExecuteQuery: size=" + el.size());		
		assertTrue(el.size() <= 3);
		
		logger().debug("testExecuteLimits: el=" + el);
	}
		
	public void testExecuteSort() throws Exception {
		
		Film.QueryElement.Builder qeb = new Film.QueryElement.Builder();
		qeb.addAllAttributes();
		
		Language.QueryElement.Builder ot = new Language.QueryElement.Builder();
		Language.QueryElement le = ot.newQueryElement();

		qeb.setQueryElement(Film.LANGUAGE, le);
		Film.QueryElement q = qeb.newQueryElement();
		
		Film.Query.Builder qb = new Film.Query.Builder(q);
		
		qb.desc(le, Language.NAME);				
		qb.asc(le, Language.LAST_UPDATE);
		qb.desc(Film.LAST_UPDATE);
				
		Film.Query qo = qb.newQuery();
				
		EntityQueryExpressionBuilder<Film.Attribute, Film.Reference, Film.Type, Film, Film.Mutable, Film.Holder, Film.Factory, Film.MetaData, Film.QueryElement> ctx = 
				new EntityQueryExpressionBuilder<Film.Attribute, Film.Reference, Film.Type, Film, Film.Mutable, Film.Holder, Film.Factory, Film.MetaData, Film.QueryElement>(qo);
		
		QueryExpression qe = ctx.getQueryExpression();
						
		String qs = qe.generate();
		logger().info("testExecuteSort: qs=" + qs);
		
		qs = qs.toLowerCase();
		
		assertTrue(qs.toUpperCase().matches(".+ORDER +BY +.*NAME DESC,.*LAST_UPDATE ASC,.*LAST_UPDATE DESC"));
		// R1.name DESC,R1.last_update ASC,R.last_update DESC
		
	}

	public void testNullPredicate() throws Exception {
		Film.QueryElement.Builder ht = new Film.QueryElement.Builder();
		ht.addAllAttributes();
		
		Language.QueryElement.Builder ot = new Language.QueryElement.Builder();
		Language.QueryElement le = ot.newQueryElement();

		ht.setQueryElement(Film.LANGUAGE, le);
		
		Film.QueryElement q = ht.newQueryElement();
		
		Film.Query.Builder qb = new Film.Query.Builder(q);
		qb.addPredicate(Film.FILM_ID, IntegerHolder.NULL_HOLDER);
		qb.desc(le, Language.NAME);
		Query qo = qb.newQuery();
		
		QueryResult<EntityDataObject<Film>> qr = execute(qo, null);
		assertNotNull(qr);
		assertEquals(0, qr.size());
		
	}
	
	
//	public void testNullPredicate() throws Exception {
//		// PredicateAttributeTemplate<Film.Attribute> p = PredicateAttributeTemplate.eq(Film.Attribute.FILM_ID, (Integer) null);		
//		 
//		Builder qb = newQueryBuilder();
//		
//		
//		
//		QueryResult<EntityDataObject<Film>> qr = executeWithPredicate(p);
//		assertNotNull(qr);
//		assertEquals(0, qr.size());
//	}
	
	public void testIntPredicate() throws Exception {
//		PredicateAttributeTemplate<Film.Attribute> p = 
//			PredicateAttributeTemplate.eq(Film.Attribute.FILM_ID, Integer.valueOf(30));
		
		Film.Query.Builder qb = newQueryBuilder();
		
		qb.addPredicate(Film.FILM_ID, IntegerHolder.valueOf(30));
		
		Film.Query qo = qb.newQuery();
		
		QueryResult<EntityDataObject<Film>> qr = execute(qo, null);
		assertNotNull(qr);
		assertEquals(1, qr.size());
	}
	
	
	public void testStringPredicate() throws Exception {
		Film.Query.Builder qb = newQueryBuilder();
		
		qb.addPredicate(Film.TITLE, "BASIC EASY");
		
		Film.Query qo = qb.newQuery();
		assertNotNull(qo.predicates());
		assertFalse(qo.predicates().isEmpty());
		
				
		QueryResult<EntityDataObject<Film>> qr = execute(qo, null);
		assertNotNull(qr);
		assertEquals(1, qr.size());
	}
	
	
	public Film.Query.Builder newQueryBuilder() {
		Film.QueryElement.Builder ht = new Film.QueryElement.Builder();
		ht.addAllAttributes();
		
		Language.QueryElement.Builder ot = new Language.QueryElement.Builder();
		Language.QueryElement le = ot.newQueryElement();
		
		ht.setQueryElement(Film.LANGUAGE, le);
		
		Film.Query.Builder qb = new Film.Query.Builder(ht.newQueryElement());
		
		return qb;		
	}
	
//	public QueryResult<EntityDataObject<Film>> executeWithPredicate(EntityQueryPredicate p) throws Exception {
//		Film.QueryElement.Builder ht = new Film.QueryElement.Builder();
//		ht.addAllAttributes();
//		
////		ht.addPredicate(p);
//			
//		
//		Language.QueryElement.Builder ot = new Language.QueryElement.Builder();
//		Language.QueryElement le = ot.newQueryElement();
//		
//		ht.setQueryElement(Film.LANGUAGE, le);
//		
//			
//		
//		Film.Query.Builder qb = new Film.Query.Builder(ht.newQueryElement());
//		
//		
//		// qb.
//		
////		TODO : FIX REFACTORED
////		ht.desc(ot, Language.Attribute.NAME);
//		qb.desc(le, Language.NAME);
//		
//		
//		Film.QueryElement q = ht.newQueryElement();
//		
//		QueryResult<EntityDataObject<Film>> qr = execute(q, null);
//		assertNotNull(qr);
//		return qr;
//		
//	}

	
	public void testSharedReferenceQuery1() throws Exception {		
		Film.QueryElement.Builder hrq = new Film.QueryElement.Builder(); 
		hrq.addAllAttributes();
		
		Language.QueryElement.Builder lq = new Language.QueryElement.Builder();
		lq.addAllAttributes();
				
//		hrq.setTemplate(Film.LANGUAGE, lq);		
//		hrq.setTemplate(Film.ORIGINAL_LANGUAGE, lq);
//						
//		lq.addPredicate(PredicateAttributeTemplate.isNotNull(Language.LANGUAGE_ID));

		Language.QueryElement le = lq.newQueryElement();
		
		hrq.setQueryElement(Film.LANGUAGE, le);		
		hrq.setQueryElement(Film.ORIGINAL_LANGUAGE, le);
		
		Film.Query.Builder qb = new Film.Query.Builder(hrq.newQueryElement());
						
		EntityQueryPredicate nn = le.newNotNull(Language.LANGUAGE_ID);		
		qb.addPredicate(nn);
		
		Film.Query qo = qb.newQuery();
								
		QueryResult<EntityDataObject<Film>> qr = execute(qo, null);
		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<Film>> el = qr.getContent();
		assertNotNull(el);
		
		for (EntityDataObject<Film> eo : el) {
			assertNotNull(eo);
			Film film = eo.getRoot();
			assertNotNull(film);
			
			logger().debug("testSharedReferenceQuery: film=" + film);
			
			Language.Holder lh = film.getLanguage(Film.LANGUAGE);
			assertNotNull(lh);
			
			Language lang = lh.value();
			assertNotNull(lang);
			assertTrue(lang.isIdentified());
			
			Language.Holder oh = film.getLanguage(Film.ORIGINAL_LANGUAGE);
			assertNotNull(oh);
			assertNotNull(oh.value());
			
			assertSame(oh, lh);
		
			Entity<?, ?, ?, ?, ?, ?, ?, ?> e = film.getRef(Film.ORIGINAL_LANGUAGE.name());
			assertNotNull(e);	
		}
	}
	
	
	public void testSharedReferenceQuery2() throws Exception {		
		Film.QueryElement.Builder hrq = new Film.QueryElement.Builder(); 
		hrq.addAllAttributes();
		
		Language.QueryElement.Builder lq = new Language.QueryElement.Builder();
		lq.addAllAttributes();
		
		Language.QueryElement le = lq.newQueryElement();
				
		hrq.setQueryElement(Film.LANGUAGE, le);		
		hrq.setQueryElement(Film.ORIGINAL_LANGUAGE, le);
		
		Film.Query.Builder qb = new Film.Query.Builder(hrq.newQueryElement());
		
		Film.Query qo = qb.newQuery();
								
		QueryResult<EntityDataObject<Film>> qr = execute(qo, null);
		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<Film>> el = qr.getContent();
		assertNotNull(el);
		
		for (EntityDataObject<Film> eo : el) {
			assertNotNull(eo);
			Film film = eo.getRoot();
			assertNotNull(film);
			
			logger().debug("testSharedReferenceQuery: film=" + film);
			
			Language.Holder lh = film.getLanguage(Film.LANGUAGE);
			assertNotNull(lh);
			
			Language lang = lh.value();
			assertNotNull(lang);
			assertTrue(lang.isIdentified());
			
			Language.Holder oh = film.getLanguage(Film.ORIGINAL_LANGUAGE);
			assertNotNull(oh);
			assertNotNull(oh.value());
			
			assertSame(oh, lh);
		
			Entity<?, ?, ?, ?, ?, ?, ?, ?> e = film.getRef(Film.ORIGINAL_LANGUAGE.name());
			assertNotNull(e);			
		}
	}
		
	private UnificationContext getIdentityContext(){
		return new SimpleUnificationContext();
	}
		
	public void testExistsPredicate1() throws Exception {		
		Film.QueryElement.Builder fb = new Film.QueryElement.Builder();
		fb.addAllAttributes();
						
		Film.QueryElement qe = fb.newQueryElement();
		
		EntityQueryValue pfid = qe.value(Film.FILM_ID);
				
		FilmActor.QueryElement e2 = new FilmActor.QueryElement();		
		Film.QueryElement fe = e2.getQueryElement(FilmActor.FILM);
		EntityQueryPredicate qp = fe.newEquals(Film.FILM_ID, pfid);				
		FilmActor.Query sub = new FilmActor.Query(e2, qp);
		
		EntityQueryExistsPredicate ep = new EntityQueryExistsPredicate(sub);	
				
		assertResultEquals(new Film.Query(qe), Film.FILM_ID, "SELECT film_id FROM public.film", false);
		
		Film.Query fq1 = new Film.Query(qe, ep);
		
		assertSetEquals(fq1, Film.FILM_ID, 
				"SELECT film_id FROM film f WHERE EXISTS (SELECT * FROM public.film_actor fa WHERE fa.film_id = f.film_id)");
		
		Film.Query fq2 = new Film.Query(qe, new EntityQueryPredicates.Not(ep));

		assertSetEquals(fq2, Film.FILM_ID, 
				"SELECT film_id FROM film f WHERE NOT EXISTS (SELECT * FROM public.film_actor fa WHERE fa.film_id = f.film_id)");
	}
	
	
	public void testExistsPredicate2() throws Exception {		
		Film.QueryElement.Builder fb = new Film.QueryElement.Builder();
		fb.addAllAttributes();
						
		Film.QueryElement qe = fb.newQueryElement();
		
		EntityQueryValue pfid = qe.value(Film.FILM_ID);
				
		FilmActor.QueryElement e2 = new FilmActor.QueryElement();		
		Film.QueryElement fe = e2.getQueryElement(FilmActor.FILM);
		EntityQueryPredicate qp = fe.newEquals(Film.FILM_ID, pfid);
				
		FilmActor.Query sub = new FilmActor.Query(e2, qp);
		
		EntityQueryPredicate ep = new EntityQueryExistsPredicate(sub);
		EntityQueryPredicate np = new EntityQueryPredicates.Not(ep);
				
		Film.Query fq = new Film.Query(qe, new EntityQueryPredicates.And(ep, np));
		
		assertSetEquals(fq, Film.FILM_ID, 
				"SELECT film_id FROM film f WHERE 1 = 0");
	}
		
	
	public void testEntityPredicate() throws Exception {
		{
			Actor.Mutable ma = newEntity(Actor.Type.TYPE);		
			ma.setActorId(Integer.valueOf(1));
			
			FilmActor.QueryElement qe = new FilmActor.QueryElement();
			EntityQueryPredicate qp = qe.newEquals(FilmActor.ACTOR, ma);
					
			FilmActor.Query qo = new FilmActor.Query(qe, qp);
			assertNotNull(qo.predicates());
			assertFalse(qo.predicates().isEmpty());
				
							
			QueryResult<EntityDataObject<FilmActor>> qr = execute(qo, null);
			assertNotNull(qr);
			assertEquals(19, qr.size());
		}
		
		{
			Film.Mutable mf = newEntity(Film.Type.TYPE);		
			mf.setFilmId(Integer.valueOf(1));
			
			FilmActor.QueryElement qe = new FilmActor.QueryElement();
			EntityQueryPredicate qp = qe.newEquals(FilmActor.FILM, mf);
					
			FilmActor.Query qo = new FilmActor.Query(qe, qp);
			assertNotNull(qo.predicates());
			assertFalse(qo.predicates().isEmpty());
											
			QueryResult<EntityDataObject<FilmActor>> qr = execute(qo, null);
			assertNotNull(qr);
			assertEquals(10, qr.size());
		}		
	}	

}
