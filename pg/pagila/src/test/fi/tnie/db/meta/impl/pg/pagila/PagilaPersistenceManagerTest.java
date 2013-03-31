/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg.pagila;

import java.sql.Connection;

import fi.tnie.db.PersistenceManager;
import fi.tnie.db.AbstractPersistenceManagerTest;
import fi.tnie.db.SimpleUnificationContext;
import fi.tnie.db.ent.UnificationContext;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.gen.pagila.ent.pub.Actor;
import fi.tnie.db.gen.pagila.ent.pub.Film;
import fi.tnie.db.gen.pagila.ent.pub.FilmActor;
import fi.tnie.db.gen.pagila.ent.pub.Language;
import fi.tnie.db.gen.pagila.ent.pub.PublicFactory;
import fi.tnie.db.gen.pagila.ent.pub.Actor.Factory;
import fi.tnie.db.gen.pagila.ent.pub.Actor.Holder;
import fi.tnie.db.gen.pagila.ent.pub.Actor.MetaData;
import fi.tnie.db.gen.pagila.ent.pub.Actor.Reference;
import fi.tnie.db.gen.pagila.ent.pub.Actor.Type;
import fi.tnie.db.gen.pagila.ent.pub.impl.PublicFactoryImpl;
import fi.tnie.db.test.PagilaPersistenceContext;

public class PagilaPersistenceManagerTest
	extends AbstractPersistenceManagerTest<PGImplementation> {
		
	private UnificationContext unificationContext = new SimpleUnificationContext();
	
    public void testMerge() 
	    throws Exception {
	    
	    Connection c = getConnection();
	    assertFalse(c.getAutoCommit());
	    
	    PublicFactory pf = new PublicFactoryImpl(); 
	    
	    Actor a = pf.newActor();
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
	            
	    PublicFactory pf = new PublicFactoryImpl(); 
	    
	    Actor a = pf.newActor();
	    Actor.Content ac = a.getContent();        
	    ac.setFirstName("Dana");
	    ac.setLastName("Brooks");
	
	    Film f = pf.newFilm();
	    Film.Content fc = f.getContent();
	    fc.setTitle("New Film");
	    	    
	    Language lang = pf.newLanguage();
	    lang.getContent().setName("English");	    	    
	    f.setLanguage(Film.LANGUAGE_ID_FKEY, lang.ref());
	    
	    FilmActor filmActor = pf.newFilmActor();
	    
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
