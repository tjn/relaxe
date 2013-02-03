/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.SQLException;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Content;
import fi.tnie.db.ent.CyclicTemplateException;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.gen.pg.ent.pub.Actor;
import fi.tnie.db.gen.pg.ent.pub.Film;
import fi.tnie.db.gen.pg.ent.pub.FilmActor;
import fi.tnie.db.gen.pg.ent.pub.PublicFactory;
import fi.tnie.db.gen.pg.ent.pub.Actor.Factory;
import fi.tnie.db.gen.pg.ent.pub.Actor.Holder;
import fi.tnie.db.gen.pg.ent.pub.Actor.MetaData;
import fi.tnie.db.gen.pg.ent.pub.Actor.Reference;
import fi.tnie.db.gen.pg.ent.pub.Actor.Type;
import fi.tnie.db.gen.pg.ent.pub.impl.PublicFactoryImpl;
import fi.tnie.db.meta.DBMetaTestCase;
import fi.tnie.db.query.QueryException;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public class PersistenceManagerTest extends DBMetaTestCase  {
		
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
	    
	    FilmActor filmActor = pf.newFilmActor();
	    
	    filmActor.setActor(FilmActor.ACTOR_ID_FKEY, a.ref());
	    filmActor.setFilm(FilmActor.FILM_ID_FKEY, f.ref());
	    	    	    
	    merge(filmActor, implementation(), c);
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
	}

	protected <
		A extends Attribute, 
		R extends fi.tnie.db.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends Content
	>
	PersistenceManager<A, R, T, E, H, F, M, C> create(E e) {
		PersistenceManager<A, R, T, E, H, F, M, C> pm = new PersistenceManager<A, R, T, E, H, F, M, C>(e, implementation(), null);
		return pm;
	}
	
	
	protected <
		A extends Attribute, 
		R extends fi.tnie.db.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends Content
	>
	void merge(E e, Implementation impl, Connection c) throws CyclicTemplateException, EntityException, SQLException, QueryException {
		PersistenceManager<A, R, T, E, H, F, M, C> pm = new PersistenceManager<A, R, T, E, H, F, M, C>(e, impl, null);
		pm.merge(c);		
	}
	
	protected <
		A extends Attribute, 
		R extends fi.tnie.db.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends Content
	>
	void delete(E e, Implementation impl, Connection c) throws CyclicTemplateException, EntityException, SQLException, QueryException {
		PersistenceManager<A, R, T, E, H, F, M, C> pm = new PersistenceManager<A, R, T, E, H, F, M, C>(e, impl, null);
		pm.delete(c);		
	}
	
	protected <
		A extends Attribute, 
		R extends fi.tnie.db.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends Content
	>
	void merge(E e) throws CyclicTemplateException, EntityException, SQLException, QueryException {		
		merge(e, implementation(), getConnection());		
	}
	
	protected <
		A extends Attribute, 
		R extends fi.tnie.db.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends Content
	>
	void delete(E e) throws CyclicTemplateException, EntityException, SQLException, QueryException {
		merge(e, implementation(), getConnection());		
	}		
}
