/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.mysql.sakila;

import java.sql.Connection;

import com.appspot.relaxe.AbstractPersistenceManagerTest;
import com.appspot.relaxe.PersistenceManager;
import com.appspot.relaxe.SimpleUnificationContext;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.env.mysql.MySQLImplementation;
import com.appspot.relaxe.gen.sakila.ent.sakila.Actor;
import com.appspot.relaxe.gen.sakila.ent.sakila.Film;
import com.appspot.relaxe.gen.sakila.ent.sakila.FilmActor;
import com.appspot.relaxe.gen.sakila.ent.sakila.Language;
import com.appspot.relaxe.gen.sakila.ent.sakila.Actor.Factory;
import com.appspot.relaxe.gen.sakila.ent.sakila.Actor.Holder;
import com.appspot.relaxe.gen.sakila.ent.sakila.Actor.MetaData;
import com.appspot.relaxe.gen.sakila.ent.sakila.Actor.Reference;
import com.appspot.relaxe.gen.sakila.ent.sakila.Actor.Type;
import com.appspot.relaxe.pg.pagila.SakilaPersistenceContext;


public class SakilaPersistenceManagerTest
	extends AbstractPersistenceManagerTest<MySQLImplementation> {
		
	private UnificationContext unificationContext = new SimpleUnificationContext();

    public void testMerge() 
    	throws Exception {
    
	    Connection c = getConnection();
	    assertFalse(c.getAutoCommit());
	    
	    // SakilaFactory sf = new SakilaFactoryImpl(); 
	    
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
	protected PersistenceContext<MySQLImplementation> createPersistenceContext() {
		return new SakilaPersistenceContext();
	}
	
	@Override
	protected String getDatabase() {
		return "sakila";
	}	
}
