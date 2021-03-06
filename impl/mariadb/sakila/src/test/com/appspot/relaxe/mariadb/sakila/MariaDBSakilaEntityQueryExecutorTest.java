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
package com.appspot.relaxe.mariadb.sakila;

import java.sql.Connection;
import java.util.List;

import com.appspot.relaxe.EntityQueryExecutor;
import com.appspot.relaxe.SimpleUnificationContext;
import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.DataObjectQueryResult;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityDataObject;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.EntityQueryResult;
import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.gen.mariadb.sakila.ent.sakila.Film;
import com.appspot.relaxe.gen.mariadb.sakila.ent.sakila.Language;
import com.appspot.relaxe.query.QueryResult;
import com.appspot.relaxe.rdbms.PersistenceContext;
import com.appspot.relaxe.rdbms.mariadb.MariaDBImplementation;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.IntegerHolder;
import com.appspot.relaxe.value.ReferenceHolder;
import com.appspot.relaxe.value.VarcharHolder;

public class MariaDBSakilaEntityQueryExecutorTest 
	extends MariaDBSakilaTestCase {
	
	private QueryResult<EntityDataObject<Film>> execute(Film.Query query) throws Exception {
		return execute(query, null);		
	}
	
	private QueryResult<EntityDataObject<Film>> execute(Film.Query query, FetchOptions opts) throws Exception {
		PersistenceContext<MariaDBImplementation> pc = getPersistenceContext();
		
		Connection c = newConnection();
				
		try {
			EntityQueryExecutor<
				Film.Attribute, 
				Film.Reference, 
				Film.Type, 
				Film, 
				Film.Mutable, 
				Film.Holder,
				Film.Factory, 
				Film.MetaData,				
				Film.QueryElement				
			> qe = createExecutor(Film.Type.TYPE.getMetaData(), pc);
			
			
			// Query q = template.newQuery(limit, offset);
						
			EntityQueryResult<Film.Attribute, Film.Reference, Film.Type, Film, Film.Mutable, Film.Holder, Film.Factory, Film.MetaData, Film.QueryElement> er = qe.execute(query, opts, c);
			assertNotNull(er);
			
			DataObjectQueryResult<EntityDataObject<Film>> qr = er.getContent(); 
			// QueryResult<EntityDataObject<Film>> qr = qe.execute(q, true, c);
			assertNotNull(qr);
			
			DataObject.MetaData meta = qr.getMeta();
			assertNotNull(meta);
			
			int cc = meta.getColumnCount();
			assertTrue(cc > 0);
			
			for (int i = 0; i < cc; i++) {
				ValueExpression ve = meta.expr(i);
				assertNotNull(ve);
			}
			
			
			return qr;			
		}
		finally {
			if (c != null) {
				c.close();
			}
		}
	}

	public <
		A extends AttributeName,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>,
		QE extends EntityQueryElement<A, R, T, E, B, H, F, M, QE>
	>
	EntityQueryExecutor<A, R, T, E, B, H, F, M, QE> createExecutor(M meta, PersistenceContext<?> persistenceContext) {
		return new EntityQueryExecutor<A, R, T, E, B, H, F, M, QE>(persistenceContext, getIdentityContext());
	}
	
	
	public void testExecute3() throws Exception {		
		Film.QueryElement.Builder hrq = new Film.QueryElement.Builder(); 
		hrq.addAllAttributes();
		
		Language.QueryElement.Builder lq = new Language.QueryElement.Builder();
		lq.addAllAttributes();
		
		Language.QueryElement le = lq.newQueryElement();
		hrq.setQueryElement(Film.LANGUAGE, le);
		
		Film.Query qo = new Film.Query(hrq.newQueryElement());
				
		QueryResult<EntityDataObject<Film>> qr = execute(qo, null);
		
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
				
//		for (Language.Attribute a : Language.Type.TYPE.getMetaData().attributes()) {
//			oq.remove(a);	
//		}
		
		Language.QueryElement le = oq.newQueryElement();
		hrq.setQueryElement(Film.LANGUAGE, le);
		
		Film.Query qo = new Film.Query(hrq.newQueryElement());
						
		QueryResult<EntityDataObject<Film>> qr = execute(qo);
		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<Film>> el = qr.getContent();
		assertNotNull(el);
		
		for (EntityDataObject<Film> eo : el) {
			assertNotNull(eo);
			Film hr = eo.getRoot();
			assertNotNull(hr);
			
			Language.Holder oh = hr.getLanguage(Film.LANGUAGE);
			assertNotNull(oh);
			Language org = oh.value();
			assertNotNull(org);
			assertTrue(org.isIdentified());
		}
	}
	
	public void testExecute5() throws Exception {
		Film.QueryElement.Builder hrq = new Film.QueryElement.Builder(); 
		hrq.addAllAttributes();
							
		hrq.setQueryElement(Film.LANGUAGE, (Language.QueryElement) null);
		
		Film.Query query = new Film.Query(hrq.newQueryElement());
		
		QueryResult<EntityDataObject<Film>> qr = execute(query);
		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<Film>> el = qr.getContent();
		assertNotNull(el);
		
		for (EntityDataObject<Film> eo : el) {
			assertNotNull(eo);
			Film hr = eo.getRoot();
			assertNotNull(hr);
			
			Language.Holder oh = hr.getLanguage(Film.LANGUAGE);
			assertNull(oh);
		}
	}
	
	public void testExecute6() throws Exception {
		Film.QueryElement.Builder hrq = new Film.QueryElement.Builder(); 
		hrq.addAllAttributes();
		
		Language.QueryElement.Builder jt = new Language.QueryElement.Builder();
		jt.addAllAttributes();		
		hrq.setQueryElement(Film.LANGUAGE_ORIGINAL, jt.newQueryElement());
							
		
		Language.QueryElement.Builder ot = new Language.QueryElement.Builder();
		ot.removeAll(Language.Type.TYPE.getMetaData().attributes());
		
		hrq.setQueryElement(Film.LANGUAGE, ot.newQueryElement());
		
//		Person.QueryElement.Builder pt = new Person.QueryElement.Builder();
//		pt.add(Person.Attribute.DATE_OF_BIRTH, Person.Attribute.LAST_NAME);
//		ot.setQueryElement(Language.FK_COMPANY_CEO, pt);
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
//			Language.Holder jh = hr.getLanguage(Film.LANGUAGE_ORIGINAL);
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
		
		
		Film.Query qo = new Film.Query(hrq.newQueryElement());
		
		QueryResult<EntityDataObject<Film>> qr = execute(qo, new FetchOptions(3, 3));		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<Film>> el = qr.getContent();
		assertNotNull(el);
		
		logger().debug("testExecuteQuery: size=" + el.size());		
		assertTrue(el.size() <= 3);
		
		Film.Query q36 = new Film.Query(hrq.newQueryElement());
				
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
		
		Film.QueryElement.Builder hrq = new Film.QueryElement.Builder();
		hrq.addAllAttributes();
		
		Language.QueryElement.Builder ot = new Language.QueryElement.Builder();		
		final Language.QueryElement le = ot.newQueryElement();

		hrq.setQueryElement(Film.LANGUAGE, le);
		
		Film.Query.Builder qb = new Film.Query.Builder(hrq.newQueryElement());
	
		qb.desc(le, Language.NAME);
		qb.asc(le, Language.LAST_UPDATE);
		qb.desc(Film.LAST_UPDATE);
						
//		logger().info("testExecuteSort: q.getColumnMap()=" + q.getColumnMap());
		
		Film.Query qo = qb.newQuery();
		
		QueryExpression qe = toQueryExpression(qo);
						
		String qs = qe.generate();
		logger().info("testExecuteSort: qs=" + qs);
		
		// qs = qs.toLowerCase();
		
		assertTrue(qs.matches(".+ORDER +BY.+"));
		
	}

	public void testNotNullPredicate() throws Exception {
		Film.QueryElement.Builder ht = new Film.QueryElement.Builder();
		ht.addAllAttributes();

		Language.QueryElement.Builder ot = new Language.QueryElement.Builder();		
		Language.QueryElement le = ot.newQueryElement();

		ht.setQueryElement(Film.LANGUAGE, le);
				
//		PredicateAttributeTemplate<Film.Attribute> p = 
//		PredicateAttributeTemplate.eq(Film.Attribute.FILM_ID, (Integer) null);
	
//	ht.addPredicate(p);
		Film.QueryElement root = ht.newQueryElement();
		
		Film.Query.Builder b = new Film.Query.Builder(root);
		
		b.desc(le, Language.NAME);		
		b.addPredicate(root.newNull(Film.FILM_ID));
				
		Film.Query qo = b.newQuery();
				
		QueryResult<EntityDataObject<Film>> qr = execute(qo, null);
		assertNotNull(qr);
		assertEquals(0, qr.size());
		
	}
	
	
//	public void testNullPredicate() throws Exception {
//				
//		Film.Query.Builder qb = newQueryBuilder();
//		
//		qb.addPredicate(Film.FILM_ID, IntegerHolder.NULL_HOLDER);
//		
//		Film.Query qo = qb.newQuery();
//
//		
//		QueryResult<EntityDataObject<Film>> qr = executeWithPredicate(p);
//		assertNotNull(qr);
//		assertEquals(0, qr.size());
//	}
	
	public void testIntPredicate() throws Exception {
		
		Film.Query.Builder qb = newQueryBuilder();
		
		Film.QueryElement re = qb.getRootElement();
		
		qb.addPredicate(re.newEquals(Film.FILM_ID, IntegerHolder.valueOf(30)));
//		PredicateAttributeTemplate<Film.Attribute> p = 
//			PredicateAttributeTemplate.eq(Film.Attribute.FILM_ID, Integer.valueOf(30));
//		
//		QueryResult<EntityDataObject<Film>> qr = executeWithPredicate(p);
//		assertNotNull(qr);
//		assertEquals(1, qr.size());
	}
	
	
	public void testStringPredicate() throws Exception {
//		PredicateAttributeTemplate<Film.Attribute> p = 
//			PredicateAttributeTemplate.eq(Film.Attribute.TITLE, "BASIC EASY");
		
		Film.Query.Builder qb = newQueryBuilder();
		Film.QueryElement re = qb.getRootElement();
		qb.addPredicate(re.newEquals(Film.TITLE, VarcharHolder.valueOf("BASIC EASY")));
		
		Film.Query qo = qb.newQuery();
		
		QueryResult<EntityDataObject<Film>> qr = execute(qo, null);
		assertNotNull(qr);
		assertEquals(1, qr.size());
	}
	
	public Film.Query.Builder newQueryBuilder() throws Exception {
		Film.QueryElement.Builder ht = new Film.QueryElement.Builder();
		ht.addAllAttributes();
					
		Language.QueryElement.Builder ot = new Language.QueryElement.Builder();		
		Language.QueryElement le = ot.newQueryElement();

		ht.setQueryElement(Film.LANGUAGE, le);
		
		Film.QueryElement q = ht.newQueryElement();
		
		Film.Query.Builder qb = new Film.Query.Builder(q);
		return qb;
	}

	
	public void testSharedReferenceQuery1() throws Exception {		
		Film.QueryElement.Builder hrq = new Film.QueryElement.Builder(); 
		hrq.addAllAttributes();
		
		Language.QueryElement.Builder lq = new Language.QueryElement.Builder();
		lq.addAllAttributes();
		
		Language.QueryElement le = lq.newQueryElement();
				
		hrq.setQueryElement(Film.LANGUAGE, le);		
		hrq.setQueryElement(Film.LANGUAGE_ORIGINAL, le);
		
		Film.Query.Builder qb = new Film.Query.Builder(hrq.newQueryElement());
								
		qb.addPredicate(le.newNotNull(Language.LANGUAGE_ID));
		
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
			
			Language.Holder oh = film.getLanguage(Film.LANGUAGE_ORIGINAL);
			assertNotNull(oh);
			assertNotNull(oh.value());
			
			assertSame(oh, lh);
		
			Entity<?, ?, ?, ?, ?, ?, ?, ?> e = film.getRef(Film.LANGUAGE_ORIGINAL.name());
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
		hrq.setQueryElement(Film.LANGUAGE_ORIGINAL, le);
		
		Film.Query qo = new Film.Query(hrq.newQueryElement());
								
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
			
			Language.Holder oh = film.getLanguage(Film.LANGUAGE_ORIGINAL);
			assertNotNull(oh);
			assertNotNull(oh.value());
			
			assertSame(oh, lh);
		
			Entity<?, ?, ?, ?, ?, ?, ?, ?> e = film.getRef(Film.LANGUAGE_ORIGINAL.name());
			assertNotNull(e);			
		}
	}
	
	
	
		
	private UnificationContext getIdentityContext(){
		return new SimpleUnificationContext();
	}
}
