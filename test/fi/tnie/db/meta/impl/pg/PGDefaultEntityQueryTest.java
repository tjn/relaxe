/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.sql.Connection;
import java.sql.Statement;

import fi.tnie.db.EntityReader;
import fi.tnie.db.SimpleUnificationContext;
import fi.tnie.db.StatementExecutor;
import fi.tnie.db.ent.EntityDataObject;
import fi.tnie.db.ent.UnificationContext;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.pg.PGCatalogFactory;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.expr.SelectStatement;
import fi.tnie.db.gen.pg.ent.LiteralCatalog;
import fi.tnie.db.gen.pg.ent.pub.Actor;
import fi.tnie.db.gen.pg.ent.pub.Category;
import fi.tnie.db.gen.pg.ent.pub.Film;
import fi.tnie.db.gen.pg.ent.pub.FilmActor;
import fi.tnie.db.gen.pg.ent.pub.FilmCategory;
import fi.tnie.db.gen.pg.ent.pub.PublicFactory;
import fi.tnie.db.meta.DBMetaTestCase;

public class PGDefaultEntityQueryTest extends DBMetaTestCase {

    @Override
	public PGCatalogFactory factory() {    	
    	implementation();
    	
        PGImplementation e = new PGImplementation();
        return new PGCatalogFactory(e.getEnvironment());
    }
    
    @Override
    protected Implementation implementation() {
    	return new PGImplementation();
    }
    
    public void testQuery1() throws Exception {
    	Connection c = getConnection();
    	assertNotNull(c);
    	logger().debug(c.getMetaData().getURL());
    	
    	Implementation imp = implementation();
    	    	
    	LiteralCatalog cc = LiteralCatalog.getInstance();
    	    	
    	FilmActor.QueryTemplate t = new FilmActor.QueryTemplate();
    	FilmActor.Query faq = t.newQuery();    	
    	
    	String qs = faq.getQueryExpression().generate();
    	    	   	
    	logger().info("testThis: qs=" + qs);
    	    	
    	Statement st = c.createStatement();
    	UnificationContext ic = new SimpleUnificationContext();
    	    	    	    	
    	EntityReader<FilmActor.Attribute, FilmActor.Reference, FilmActor.Type, FilmActor, FilmActor.Holder, FilmActor.Factory, FilmActor.MetaData, FilmActor.Content> eb
    		= new EntityReader<FilmActor.Attribute, FilmActor.Reference, FilmActor.Type, FilmActor, FilmActor.Holder, FilmActor.Factory, FilmActor.MetaData, FilmActor.Content>(imp, faq, ic);
    	    	    	    	
    	PGImplementation impl = new PGImplementation();    	
    	StatementExecutor se = new StatementExecutor(impl);
    	
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
    	
    	Implementation imp = implementation();
    	    	
    	LiteralCatalog cc = LiteralCatalog.getInstance();
    	PublicFactory pf = cc.newPublicFactory();
    	    	
    	Film film = pf.newFilm();
    	    	
    	Category category = pf.newCategory();
    	category.getContent().setName("project name");
    	
    	Actor actor = pf.newActor();
    	Actor.Content ac = actor.getContent();
    	ac.firstName().set("Emilio");
    	ac.lastName().set("Bullock");
    	    	
    	FilmCategory fc = pf.newFilmCategory();
    	fc.setFilm(FilmCategory.FILM_ID_FKEY, film.ref());
    	fc.setCategory(FilmCategory.CATEGORY_ID_FKEY, category.ref());
    	
    	
//    	Project.Key<?, HourReport.Reference, HourReport.Type, HourReport, ?, ?, HourReport.MetaData, ?> k = HourReport.FK_HHR_PROJECT;
//    	Holder<?, ?> h = k.get(hr);
//    	assertNotNull(h);
//    	assertSame(filmCategory.ref(), h);
    	
//    	DefaultEntityQuery<?, ?, ?, ?, ?> e = 
//    		new DefaultEntityQuery<HourReport.Attribute, HourReport.Reference, HourReport.Type, HourReport, HourReport.MetaData>(hr);
    	
    	FilmActor.QueryTemplate t = new FilmActor.QueryTemplate();
    	FilmActor.Query faq = t.newQuery();    	
    	
    	String qs = faq.getQueryExpression().generate();
    	    	   	
    	logger().info("testThis: qs=" + qs);
    	    	
    	Statement st = c.createStatement();
    	
//    	ValueExtractorFactory vef = imp.getValueExtractorFactory();
    	
    	UnificationContext ic = new SimpleUnificationContext();
    	    	    	    	
    	EntityReader<FilmActor.Attribute, FilmActor.Reference, FilmActor.Type, FilmActor, FilmActor.Holder, FilmActor.Factory, FilmActor.MetaData, FilmActor.Content> eb
    		= new EntityReader<FilmActor.Attribute, FilmActor.Reference, FilmActor.Type, FilmActor, FilmActor.Holder, FilmActor.Factory, FilmActor.MetaData, FilmActor.Content>(imp, faq, ic);
    	    	    	    	
    	PGImplementation impl = new PGImplementation();
    	
    	StatementExecutor se = new StatementExecutor(impl);
    	
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
