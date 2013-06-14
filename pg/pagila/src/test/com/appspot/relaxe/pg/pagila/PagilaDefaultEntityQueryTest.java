/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import java.sql.Connection;
import java.sql.Statement;

import com.appspot.relaxe.EntityReader;
import com.appspot.relaxe.SimpleUnificationContext;
import com.appspot.relaxe.StatementExecutor;
import com.appspot.relaxe.ValueExtractorFactory;
import com.appspot.relaxe.ent.EntityDataObject;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.env.Implementation;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.env.pg.PGCatalogFactory;
import com.appspot.relaxe.env.pg.PGImplementation;
import com.appspot.relaxe.expr.SelectStatement;
import com.appspot.relaxe.gen.pagila.ent.pub.Actor;
import com.appspot.relaxe.gen.pagila.ent.pub.Category;
import com.appspot.relaxe.gen.pagila.ent.pub.Film;
import com.appspot.relaxe.gen.pagila.ent.pub.FilmActor;
import com.appspot.relaxe.gen.pagila.ent.pub.FilmCategory;
import com.appspot.relaxe.meta.impl.pg.PGTestCase;


public class PagilaDefaultEntityQueryTest 
	extends PGTestCase {
	
    @Override
	public PGCatalogFactory factory() {   	
    	return new PGCatalogFactory(implementation().getEnvironment());
    }
        
    public void testQuery1() throws Exception {
    	Connection c = getConnection();
    	assertNotNull(c);
    	logger().debug(c.getMetaData().getURL());
    	
    	PersistenceContext<PGImplementation> pc = getPersistenceContext();    	    	    	
//    	LiteralCatalog cc = LiteralCatalog.getInstance();
    	    	
    	FilmActor.QueryTemplate t = new FilmActor.QueryTemplate();
    	FilmActor.Query faq = t.newQuery();    	
    	
    	String qs = faq.getQueryExpression().generate();
    	    	   	
    	logger().info("testThis: qs=" + qs);
    	    	
    	Statement st = c.createStatement();
    	UnificationContext ic = new SimpleUnificationContext();
    	
    	ValueExtractorFactory vef = pc.getValueExtractorFactory();
    	    	    	    	
    	EntityReader<FilmActor.Attribute, FilmActor.Reference, FilmActor.Type, FilmActor, FilmActor.Holder, FilmActor.Factory, FilmActor.MetaData, FilmActor.Content> eb
    		= new EntityReader<FilmActor.Attribute, FilmActor.Reference, FilmActor.Type, FilmActor, FilmActor.Holder, FilmActor.Factory, FilmActor.MetaData, FilmActor.Content>(vef, faq, ic);
    	    	
    	StatementExecutor se = new StatementExecutor(pc);
    	
    	SelectStatement ss = new SelectStatement(eb.getQuery().getQueryExpression());
    	    	
    	se.execute(ss, c, eb);
    	
    	logger().info("testConstructor: eb.getContent().size()=" + eb.getContent().size());    	
    	st.close();   	
    	    	
    	for (EntityDataObject<FilmActor> o : eb.getContent()) {
//			logger().info("testConstructor: rpt=" + o);
			FilmActor root = o.getRoot();			
//			logger()debug("testConstructor: root=" + root);
			
			assertNull(root.getContent().lastUpdate().getHolder());
			
			{
				Actor.Holder ah = root.getActor(FilmActor.ACTOR_ID_FKEY);
				assertNotNull(ah);
				assertFalse(ah.isNull());
				assertNotNull(ah.value().getContent().getActorId());
				assertEquals(1, ah.value().attributes().size());
			}
			
			{
				Film.Holder fh = root.getFilm(FilmActor.FILM_ID_FKEY);
				assertNotNull(fh);
				assertFalse(fh.isNull());
				assertNotNull(fh.value().getContent().getFilmId());
				assertEquals(1, fh.value().attributes().size());
			}
		}
    }
    
    
    public void testQuery2() throws Exception {
    	Connection c = getConnection();
    	assertNotNull(c);
    	logger().debug(c.getMetaData().getURL());
    	
    	Implementation<?> imp = implementation();
    	    	
//    	LiteralCatalog cc = LiteralCatalog.getInstance();
    	// PublicFactory pf = new PublicFactoryImpl();
    	
    	Film film = newEntity(Film.Type.TYPE);
    	
    	Category category = newEntity(Category.Type.TYPE);
    	category.getContent().setName("project name");
    	
    	Actor actor = newEntity(Actor.Type.TYPE);
    	Actor.Content ac = actor.getContent();
    	ac.firstName().set("Emilio");
    	ac.lastName().set("Bullock");
    	    	
    	FilmCategory fc = newEntity(FilmCategory.Type.TYPE);
    	fc.setFilm(FilmCategory.FILM_ID_FKEY, film.ref());
    	fc.setCategory(FilmCategory.CATEGORY_ID_FKEY, category.ref());
    	
    	FilmActor.QueryTemplate t = new FilmActor.QueryTemplate();
    	FilmActor.Query faq = t.newQuery();    	
    	
    	String qs = faq.getQueryExpression().generate();
    	    	   	
    	logger().info("testThis: qs=" + qs);
    	    	
    	Statement st = c.createStatement();
    	
//    	ValueExtractorFactory vef = imp.getValueExtractorFactory();
    	
    	UnificationContext ic = new SimpleUnificationContext();
    	
    	ValueExtractorFactory vef = imp.getValueExtractorFactory();
    	    	    	    	
    	EntityReader<FilmActor.Attribute, FilmActor.Reference, FilmActor.Type, FilmActor, FilmActor.Holder, FilmActor.Factory, FilmActor.MetaData, FilmActor.Content> eb
    		= new EntityReader<FilmActor.Attribute, FilmActor.Reference, FilmActor.Type, FilmActor, FilmActor.Holder, FilmActor.Factory, FilmActor.MetaData, FilmActor.Content>(vef, faq, ic);
    	   	
    	StatementExecutor se = new StatementExecutor(getPersistenceContext());
    	
    	SelectStatement ss = new SelectStatement(eb.getQuery().getQueryExpression());
    	se.execute(ss, c, eb);
    	
    	logger().info("testConstructor: eb.getContent().size()=" + eb.getContent().size());
    	
//    	ResultSet rs = st.executeQuery(qs);
//    	rs.next();
//    	rs.close();
    	st.close();   	
    	    	
    	for (EntityDataObject<FilmActor> o : eb.getContent()) {
			logger().info("testConstructor: rpt=" + o);
			FilmActor root = o.getRoot();			
			logger().debug("testConstructor: root=" + root);
			
			Actor.Holder ah = root.getActor(FilmActor.ACTOR_ID_FKEY);
			assertNotNull(ah);
			assertFalse(ah.isNull());	
						
			Film.Holder fh = root.getFilm(FilmActor.FILM_ID_FKEY);
			assertNotNull(fh);
			assertFalse(ah.isNull());
			
			
			Actor.Content acc = ah.value().getContent();
			assertNotNull(acc);
			assertNotNull(acc.firstName());
						
			
			// Project.Holder ph = root.getProject(HourReport.FK_HHR_PROJECT);
//			logger().info("testConstructor: rpt.project=" + ph);    		
		}
    }
    
    
//    public void testConstructor2() throws CyclicTemplateException2, SQLException {
//    	Connection c = getConnection();
//    	assertNotNull(c);
//    	    	
//    	LiteralCatalog cc = LiteralCatalog.getInstance();
//    	PersonalFactory pf = cc.newPersonalFactory();
//    	    	
//    	HourReport hr = pf.newHourReport();
//    	
//    	DefaultEntityQuery<?, ?, ?, ?, ?, ?> e = 
//    		new DefaultEntityQuery<HourReport.Attribute, HourReport.Reference, HourReport.Type, HourReport, HourReport.Factory, HourReport.MetaData>(hr.getMetaData());
//
//    	String qs = e.getQueryExpression().generate();
//    	
//    	logger().info("testConstructor2: qs=" + qs);
//    	
//    	Statement st = c.createStatement();
//    	
//    	ResultSet rs = st.executeQuery(qs);
//    	
//    	rs.next();
//    	rs.close();
//    	st.close();  	
//    	
//    }
    
    
//    public void testCreate() 
//        throws Exception {        
//        PGCatalogFactory factory = factory();
//        testCatalogFactory(factory, getConnection());
//    }
    

}
