/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import java.sql.Connection;
import java.util.Set;

import com.appspot.relaxe.AbstractPersistenceManagerTest;
import com.appspot.relaxe.PersistenceManager;
import com.appspot.relaxe.SimpleUnificationContext;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.env.pg.PGImplementation;
import com.appspot.relaxe.gen.pagila.ent.pub.Actor;
import com.appspot.relaxe.gen.pagila.ent.pub.Actor.Attribute;
import com.appspot.relaxe.gen.pagila.ent.pub.Film;
import com.appspot.relaxe.gen.pagila.ent.pub.FilmActor;
import com.appspot.relaxe.gen.pagila.ent.pub.Language;
import com.appspot.relaxe.gen.pagila.ent.pub.Actor.Factory;
import com.appspot.relaxe.gen.pagila.ent.pub.Actor.Holder;
import com.appspot.relaxe.gen.pagila.ent.pub.Actor.MetaData;
import com.appspot.relaxe.gen.pagila.ent.pub.Actor.Reference;
import com.appspot.relaxe.gen.pagila.ent.pub.Actor.Type;
import com.appspot.relaxe.pg.pagila.PagilaPersistenceContext;


public class PagilaPersistenceManagerTest
	extends AbstractPersistenceManagerTest<PGImplementation> {
		
	private UnificationContext unificationContext = new SimpleUnificationContext();
	
    public void testMerge() 
	    throws Exception {
	    
	    Connection c = getConnection();
	    assertFalse(c.getAutoCommit());
	    
	    // PublicFactory pf = new PublicFactoryImpl(); 
	    
	    Actor a = newEntity(Actor.Type.TYPE);
	    Actor.Content ac = a.getContent();        
	    ac.setFirstName("Dana");
	    ac.setLastName("Brooks");
	
	    PersistenceManager<Actor.Attribute, Reference, Type, Actor, Holder, Factory, MetaData, Actor.Content, Actor.QueryElement> pm = create(a);
	    
	    pm.merge(c);
	    c.commit();        
	    pm.delete(c);
	    c.commit();
	    pm.insert(c);
	    c.commit();
	    pm.update(c);
	    c.commit();
	    pm.delete(c);
	    c.commit();
	}
    
    public void testMergeDependent() 
	    throws Exception {
	
	    Connection c = getConnection();
	    assertFalse(c.getAutoCommit());
	    
	    final Actor a = newEntity(Actor.Type.TYPE);
	    Actor.Content ac = a.getContent();        
	    ac.setFirstName("Dana");
	    ac.setLastName("Brooks");
	
	    Film f = newEntity(Film.Type.TYPE);
	    Film.Content fc = f.getContent();
	    fc.setTitle("New Film");
	    	    
	    Language lang = newEntity(Language.Type.TYPE);
	    lang.getContent().setName("English");	    	    
	    f.setLanguage(Film.LANGUAGE, lang.ref());
	    
	    FilmActor filmActor = newEntity(FilmActor.Type.TYPE);
	    
	    filmActor.setActor(FilmActor.ACTOR, a.ref());
	    filmActor.setFilm(FilmActor.FILM, f.ref());
	    	    	    
	    merge(filmActor, getPersistenceContext(), c);
	    c.commit();    
	    
	    assertTrue(filmActor.isIdentified());
	    delete(filmActor);
	    c.commit();
	    
	    assertTrue(a.isIdentified());
	    
	    Actor sa = sync(a, getPersistenceContext(), c);
	    assertNotNull(sa);
	    Set<Attribute> ma = sa.getMetaData().attributes();
	    assertEquals(ma, sa.attributes());
	    
	    
	    delete(a);
	    c.commit();
	    
	    
	    
	    
	    assertTrue(f.isIdentified());
	    delete(f);
	    c.commit();
	    
	    assertTrue(lang.isIdentified());
	    logger().debug("testMergeDependent: lang.getContent().getLanguageId()=" + lang.getContent().getLanguageId());
	    
	    delete(lang);
	    c.commit();
	}
    
    
    
    
    
	
	public void testMergeDependent1() throws Exception {
		setUnificationContext(unificationContext);
		testMergeDependent();		
	}

	public void testMergeDependent2() throws Exception {
		setUnificationContext(null);
		testMergeDependent();
	}

	@Override
	protected PersistenceContext<PGImplementation> createPersistenceContext() {
		return new PagilaPersistenceContext();
	}

	@Override
	protected String getDatabase() {
		return "pagila";
	}
	
	@Override
	protected String getUsername() {
		return "relaxe_tester";
	}
	
    public void testSync1() 
		throws Exception {
	
	    Connection c = getConnection();
	    PersistenceContext<PGImplementation> pc = getPersistenceContext();
	    	    
	    setUnificationContext(new SimpleUnificationContext());
	    logger().debug("testSync1: getUnificationContext()=" + getUnificationContext());
	    
	    assertFalse(c.getAutoCommit());
	    
	    final Actor a = newEntity(Actor.Type.TYPE);
	    Integer id = Integer.valueOf(1);
	    a.getContent().setActorId(id);	    
	    assertEquals(a.attributes().size(), 1);
	    
	    Actor sr = sync(a, pc, c);
	    assertNotSame(a, sr);
	    
	    assertEquals(Actor.Attribute.values().length, sr.attributes().size());	    	    
	}		
	
	
    public void testSync() 
		throws Exception {
	
	    Connection c = getConnection();
	    PersistenceContext<PGImplementation> pc = getPersistenceContext();
	    assertFalse(c.getAutoCommit());
	    
	    final Actor a = newEntity(Actor.Type.TYPE);
	    Actor.Content ac = a.getContent();        
	    ac.setFirstName("Dana");
	    ac.setLastName("Brooks");
	    
	    final Actor ma = merge(a, pc, c);
	    
	    Integer aid = ma.getContent().getActorId();
	    assertNotNull(aid);
	        	
	    Set<Attribute> expected = ma.getMetaData().attributes();
	    assertEquals(ma.attributes(), expected);
	    
	    for (Actor.Attribute aa : Actor.Attribute.values()) {
			assertNotNull(aa);
			assertTrue(expected.contains(aa));
		}
	    
	    	    
	    Actor pa = newEntity(Actor.Type.TYPE);
	    pa.getContent().setActorId(aid);	    
	    assertEquals(pa.attributes().size(), 1);
	    
	    Actor sr = sync(pa, pc, c);	        	        	    
	    assertEquals(sr.attributes(), expected);
	    	    
	}	
}
