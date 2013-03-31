/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg.pagila;

import java.sql.Connection;
import java.util.List;

import fi.tnie.db.EntityQueryExecutor;
import fi.tnie.db.SimpleUnificationContext;
import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Content;
import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.DataObjectQueryResult;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityDataObject;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQueryExpressionSortKey;
import fi.tnie.db.ent.EntityQueryResult;
import fi.tnie.db.ent.EntityQueryTemplate;
import fi.tnie.db.ent.EntityQueryTemplateAttribute;
import fi.tnie.db.ent.FetchOptions;
import fi.tnie.db.ent.PredicateAttributeTemplate;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.ent.UnificationContext;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.expr.OrderBy;
import fi.tnie.db.expr.ValueExpression;
import fi.tnie.db.gen.pg.ent.pub.Film;
import fi.tnie.db.gen.pg.ent.pub.Language;
import fi.tnie.db.query.QueryResult;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public class PagilaEntityQueryExecutorTest 
	extends AbstractPagilaTestCase {
	
	private QueryResult<EntityDataObject<Film>> execute(Film.QueryTemplate template) throws Exception {
		return execute(template, null);		
	}
	
	private QueryResult<EntityDataObject<Film>> execute(Film.QueryTemplate template, FetchOptions opts) throws Exception {
		return execute(template.newQuery(), opts);
	}
	
	private QueryResult<EntityDataObject<Film>> execute(Film.Query q, FetchOptions opts) throws Exception {
		PersistenceContext<PGImplementation> pc = getPersistenceContext();
		
		Connection c = newConnection();
				
		try {
			EntityQueryExecutor<
				Film.Attribute, 
				Film.Reference, 
				Film.Type, 
				Film, 
				Film.Holder, 
				Film.Factory, 
				Film.MetaData,
				Film.Content,
				Film.QueryTemplate				
			> qe = createExecutor(Film.Type.TYPE.getMetaData(), pc);
			
			
			// Query q = template.newQuery(limit, offset);
			
			EntityQueryResult<Film.Attribute, Film.Reference, Film.Type, Film, Film.Holder, Film.Factory, Film.MetaData, Film.Content, Film.QueryTemplate> er = qe.execute(q, opts, c);
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

//	private Connection newConnection(Implementation imp) throws Exception {
//		Properties cfg = new Properties();
//		cfg.setProperty("user", "test");
//		cfg.setProperty("password", "test");
//		
//		String url = imp.createJdbcUrl("relaxe_test");
//		Class.forName(imp.defaultDriverClassName());
//		Connection c = DriverManager.getConnection(url, cfg);
//		return c;
//	}
		

	public <
		A extends Attribute,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, H, F, M, C>,
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends Content,
		QT extends EntityQueryTemplate<A, R, T, E, H, F, M, C, QT>
	>
	EntityQueryExecutor<A, R, T, E, H, F, M, C, QT> createExecutor(M meta, PersistenceContext<?> persistenceContext) {
		return new EntityQueryExecutor<A, R, T, E, H, F, M, C, QT>(persistenceContext, getIdentityContext());
	}
	
	
	public void testExecute3() throws Exception {		
		Film.QueryTemplate hrq = new Film.QueryTemplate(); 
		hrq.addAllAttributes();
		
		Language.QueryTemplate lq = new Language.QueryTemplate();
		lq.addAllAttributes();
				
		hrq.setTemplate(Film.LANGUAGE_ID_FKEY, lq);
				
		QueryResult<EntityDataObject<Film>> qr = execute(hrq, null);
		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<Film>> el = qr.getContent();
		assertNotNull(el);
		
		for (EntityDataObject<Film> eo : el) {
			assertNotNull(eo);
			Film hr = eo.getRoot();
			assertNotNull(hr);
			
			Language.Holder lh = hr.getLanguage(Film.LANGUAGE_ID_FKEY);
			assertNotNull(lh);
			
			Language lang = lh.value();
			assertNotNull(lang);
			assertTrue(lang.isIdentified());
		}
	}

	public void testExecute4() throws Exception {
		Film.QueryTemplate hrq = new Film.QueryTemplate(); 
		hrq.addAllAttributes();
				
		Language.QueryTemplate oq = new Language.QueryTemplate();		
		oq.remove(oq.getMetaData().attributes());					
		hrq.setTemplate(Film.LANGUAGE_ID_FKEY, oq);
				
		QueryResult<EntityDataObject<Film>> qr = execute(hrq);
		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<Film>> el = qr.getContent();
		assertNotNull(el);
		
		for (EntityDataObject<Film> eo : el) {
			assertNotNull(eo);
			Film hr = eo.getRoot();
			assertNotNull(hr);
			
			Language.Holder oh = hr.getLanguage(Film.LANGUAGE_ID_FKEY);
			assertNotNull(oh);
			Language org = oh.value();
			assertNotNull(org);
			assertTrue(org.isIdentified());
		}
	}
	
	public void testExecute5() throws Exception {
		Film.QueryTemplate hrq = new Film.QueryTemplate(); 
		hrq.addAllAttributes();
							
		hrq.setTemplate(Film.LANGUAGE_ID_FKEY, (Language.QueryTemplate) null);
		
		QueryResult<EntityDataObject<Film>> qr = execute(hrq);
		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<Film>> el = qr.getContent();
		assertNotNull(el);
		
		for (EntityDataObject<Film> eo : el) {
			assertNotNull(eo);
			Film hr = eo.getRoot();
			assertNotNull(hr);
			
			Language.Holder oh = hr.getLanguage(Film.LANGUAGE_ID_FKEY);
			assertNull(oh);
		}
	}
	
	public void testExecute6() throws Exception {
		Film.QueryTemplate hrq = new Film.QueryTemplate(); 
		hrq.addAllAttributes();
		
		Language.QueryTemplate jt = new Language.QueryTemplate();
		jt.addAllAttributes();		
		hrq.setTemplate(Film.ORIGINAL_LANGUAGE_ID_FKEY, jt);
							
		
		Language.QueryTemplate ot = new Language.QueryTemplate();
		ot.remove(ot.getMetaData().attributes());
		
		hrq.setTemplate(Film.LANGUAGE_ID_FKEY, ot);
		
//		Person.QueryTemplate pt = new Person.QueryTemplate();
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
//			Language.Holder jh = hr.getLanguage(Film.ORIGINAL_LANGUAGE_ID_FKEY);
//			assertNotNull(jh);
//			assertNotNull(jh.value());			
//			
//			Language.Holder oh = hr.getLanguage(Film.LANGUAGE_ID_FKEY);
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
		Film.QueryTemplate hrq = new Film.QueryTemplate();
				
		hrq.addAllAttributes();
		
		EntityQueryTemplateAttribute rd = hrq.get(Film.Attribute.LAST_UPDATE);		
				
		Film.Query q3 = hrq.newQuery();								
		
		QueryResult<EntityDataObject<Film>> qr = execute(q3, new FetchOptions(3, 3));		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<Film>> el = qr.getContent();
		assertNotNull(el);
		
		logger().debug("testExecuteQuery: size=" + el.size());		
		assertTrue(el.size() <= 3);
		
		Film.Query q36 = q3.getTemplate().newQuery();
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
		
		Film.QueryTemplate hrq = new Film.QueryTemplate();
		hrq.addAllAttributes();
		
		Language.QueryTemplate ot = new Language.QueryTemplate();

		hrq.setTemplate(Film.LANGUAGE_ID_FKEY, ot);
		hrq.desc(ot, Language.Attribute.NAME);
				
		hrq.asc(ot, Language.Attribute.LAST_UPDATE);
		hrq.desc(Film.Attribute.LAST_UPDATE);
		
		hrq.addSortKey(EntityQueryExpressionSortKey.<Film.Attribute>newSortKey(new OrderBy.OrdinalSortKey(1)));
		
		Film.Query q = hrq.newQuery();
		
		logger().info("testExecuteSort: q.getColumnMap()=" + q.getColumnMap());		
						
		String qs = q.getQueryExpression().generate();
		logger().info("testExecuteSort: qs=" + qs);
		
		qs = qs.toLowerCase();
		
		assertTrue(qs.matches(".+order +by.+1.*"));
		
	}

	public void testNotNullPredicate() throws Exception {
		Film.QueryTemplate ht = new Film.QueryTemplate();
		ht.addAllAttributes();
		
		PredicateAttributeTemplate<Film.Attribute> p = 
			PredicateAttributeTemplate.eq(Film.Attribute.FILM_ID, (Integer) null);
		
		ht.addPredicate(p);
		
		Language.QueryTemplate ot = new Language.QueryTemplate();

		ht.setTemplate(Film.LANGUAGE_ID_FKEY, ot);
		ht.desc(ot, Language.Attribute.NAME);				
		
		Film.Query q = ht.newQuery();
		
		QueryResult<EntityDataObject<Film>> qr = execute(q, null);
		assertNotNull(qr);
		assertEquals(0, qr.size());
		
	}
	
	
	public void testNullPredicate() throws Exception {
		PredicateAttributeTemplate<Film.Attribute> p = PredicateAttributeTemplate.eq(Film.Attribute.FILM_ID, (Integer) null);
		QueryResult<EntityDataObject<Film>> qr = executeWithPredicate(p);
		assertNotNull(qr);
		assertEquals(0, qr.size());
	}
	
	public void testIntPredicate() throws Exception {
		PredicateAttributeTemplate<Film.Attribute> p = 
			PredicateAttributeTemplate.eq(Film.Attribute.FILM_ID, Integer.valueOf(30));
		
		QueryResult<EntityDataObject<Film>> qr = executeWithPredicate(p);
		assertNotNull(qr);
		assertEquals(1, qr.size());
	}
	
	
	public void testStringPredicate() throws Exception {
		PredicateAttributeTemplate<Film.Attribute> p = 
			PredicateAttributeTemplate.eq(Film.Attribute.TITLE, "BASIC EASY");
		
		QueryResult<EntityDataObject<Film>> qr = executeWithPredicate(p);
		assertNotNull(qr);
		assertEquals(1, qr.size());
	}
	
	public QueryResult<EntityDataObject<Film>> executeWithPredicate(PredicateAttributeTemplate<Film.Attribute> p) throws Exception {
		Film.QueryTemplate ht = new Film.QueryTemplate();
		ht.addAllAttributes();
		
		ht.addPredicate(p);
		
		Language.QueryTemplate ot = new Language.QueryTemplate();

		ht.setTemplate(Film.LANGUAGE_ID_FKEY, ot);
		ht.desc(ot, Language.Attribute.NAME);				
		
		Film.Query q = ht.newQuery();
		
		QueryResult<EntityDataObject<Film>> qr = execute(q, null);
		assertNotNull(qr);
		return qr;
		
	}

	
	public void testSharedReferenceQuery1() throws Exception {		
		Film.QueryTemplate hrq = new Film.QueryTemplate(); 
		hrq.addAllAttributes();
		
		Language.QueryTemplate lq = new Language.QueryTemplate();
		lq.addAllAttributes();
				
		hrq.setTemplate(Film.LANGUAGE_ID_FKEY, lq);		
		hrq.setTemplate(Film.ORIGINAL_LANGUAGE_ID_FKEY, lq);
						
		lq.addPredicate(PredicateAttributeTemplate.isNotNull(Language.LANGUAGE_ID));
						
		QueryResult<EntityDataObject<Film>> qr = execute(hrq, null);
		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<Film>> el = qr.getContent();
		assertNotNull(el);
		
		for (EntityDataObject<Film> eo : el) {
			assertNotNull(eo);
			Film film = eo.getRoot();
			assertNotNull(film);
			
			logger().debug("testSharedReferenceQuery: film=" + film);
			
			Language.Holder lh = film.getLanguage(Film.LANGUAGE_ID_FKEY);
			assertNotNull(lh);
			
			Language lang = lh.value();
			assertNotNull(lang);
			assertTrue(lang.isIdentified());
			
			Language.Holder oh = film.getLanguage(Film.ORIGINAL_LANGUAGE_ID_FKEY);
			assertNotNull(oh);
			assertNotNull(oh.value());
			
			assertSame(oh, lh);
		
			Entity<?, ?, ?, ?, ?, ?, ?, ?> e = film.getRef(Film.ORIGINAL_LANGUAGE_ID_FKEY.name());
			assertNotNull(e);	
		}
	}
	
	
	public void testSharedReferenceQuery2() throws Exception {		
		Film.QueryTemplate hrq = new Film.QueryTemplate(); 
		hrq.addAllAttributes();
		
		Language.QueryTemplate lq = new Language.QueryTemplate();
		lq.addAllAttributes();
				
		hrq.setTemplate(Film.LANGUAGE_ID_FKEY, lq);		
		hrq.setTemplate(Film.ORIGINAL_LANGUAGE_ID_FKEY, lq);
								
		QueryResult<EntityDataObject<Film>> qr = execute(hrq, null);
		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<Film>> el = qr.getContent();
		assertNotNull(el);
		
		for (EntityDataObject<Film> eo : el) {
			assertNotNull(eo);
			Film film = eo.getRoot();
			assertNotNull(film);
			
			logger().debug("testSharedReferenceQuery: film=" + film);
			
			Language.Holder lh = film.getLanguage(Film.LANGUAGE_ID_FKEY);
			assertNotNull(lh);
			
			Language lang = lh.value();
			assertNotNull(lang);
			assertTrue(lang.isIdentified());
			
			Language.Holder oh = film.getLanguage(Film.ORIGINAL_LANGUAGE_ID_FKEY);
			assertNotNull(oh);
			assertNotNull(oh.value());
			
			assertSame(oh, lh);
		
			Entity<?, ?, ?, ?, ?, ?, ?, ?> e = film.getRef(Film.ORIGINAL_LANGUAGE_ID_FKEY.name());
			assertNotNull(e);			
		}
	}
		
	private UnificationContext getIdentityContext(){
		return new SimpleUnificationContext();
	}

}
