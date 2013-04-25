/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.mysql.sakila;

import java.sql.Connection;

import fi.tnie.db.PersistenceManager;
import fi.tnie.db.AbstractPersistenceManagerTest;
import fi.tnie.db.SimpleUnificationContext;
import fi.tnie.db.ent.UnificationContext;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.mysql.MySQLImplementation;
import fi.tnie.db.gen.sakila.ent.sakila.Film;
import fi.tnie.db.gen.sakila.ent.sakila.FilmActor;
import fi.tnie.db.gen.sakila.ent.sakila.Language;
import fi.tnie.db.gen.sakila.ent.sakila.Actor;
import fi.tnie.db.gen.sakila.ent.sakila.Actor.Factory;
import fi.tnie.db.gen.sakila.ent.sakila.Actor.Holder;
import fi.tnie.db.gen.sakila.ent.sakila.Actor.MetaData;
import fi.tnie.db.gen.sakila.ent.sakila.Actor.Reference;
import fi.tnie.db.gen.sakila.ent.sakila.Actor.Type;
import fi.tnie.db.test.SakilaPersistenceContext;

public class SakilaPersistenceManagerTest
	extends AbstractPersistenceManagerTest<MySQLImplementation> {
		
	private UnificationContext unificationContext = new SimpleUnificationContext();

    public void testMerge() 
    	throws Exception {
    
	    Connection c = getConnection();
	    assertFalse(c.getAutoCommit());
	    
	    // SakilaFactory sf = new SakilaFactoryImpl(); 
	    
	    Actor a = newInstance(Actor.Type.TYPE);
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
	            
	    Actor a = newInstance(Actor.Type.TYPE);
	    Actor.Content ac = a.getContent();        
	    ac.setFirstName("Dana");
	    ac.setLastName("Brooks");
	
	    Film f = newInstance(Film.Type.TYPE);
	    Film.Content fc = f.getContent();
	    fc.setTitle("New Film");
	    	    
	    Language lang = newInstance(Language.Type.TYPE);
	    lang.getContent().setName("English");	    	    
	    f.setLanguage(Film.LANGUAGE, lang.ref());
	    
	    FilmActor filmActor = newInstance(FilmActor.Type.TYPE);
	    
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
