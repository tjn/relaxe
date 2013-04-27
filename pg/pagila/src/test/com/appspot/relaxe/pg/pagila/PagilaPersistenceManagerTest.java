/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import java.sql.Connection;

import com.appspot.relaxe.AbstractPersistenceManagerTest;
import com.appspot.relaxe.PersistenceManager;
import com.appspot.relaxe.SimpleUnificationContext;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.env.pg.PGImplementation;
import com.appspot.relaxe.gen.pagila.ent.pub.Actor;
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
	
	    PersistenceManager<Actor.Attribute, Reference, Type, Actor, Holder, Factory, MetaData, Actor.Content> pm = create(a);
	    
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
	    
	    Actor a = newEntity(Actor.Type.TYPE);
	    Actor.Content ac = a.getContent();        
	    ac.setFirstName("Dana");
	    ac.setLastName("Brooks");
	
	    Film f = newEntity(Film.Type.TYPE);
	    Film.Content fc = f.getContent();
	    fc.setTitle("New Film");
	    	    
	    Language lang = newEntity(Language.Type.TYPE);
	    lang.getContent().setName("English");	    	    
	    f.setLanguage(Film.LANGUAGE_ID_FKEY, lang.ref());
	    
	    FilmActor filmActor = newEntity(FilmActor.Type.TYPE);
	    
	    filmActor.setActor(FilmActor.ACTOR_ID_FKEY, a.ref());
	    filmActor.setFilm(FilmActor.FILM_ID_FKEY, f.ref());
	    	    	    
	    merge(filmActor, getPersistenceContext(), c);
	    c.commit();    
	    
	    assertTrue(filmActor.isIdentified());	    
	    delete(filmActor);
	    c.commit();
	    
	    assertTrue(a.isIdentified());	    
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
}
