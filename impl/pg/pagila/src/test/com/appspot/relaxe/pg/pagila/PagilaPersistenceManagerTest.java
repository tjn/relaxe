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
package com.appspot.relaxe.pg.pagila;

import java.sql.Connection;
import java.util.Set;

import com.appspot.relaxe.AbstractPersistenceManagerTest;
import com.appspot.relaxe.PersistenceManager;
import com.appspot.relaxe.SimpleUnificationContext;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Actor;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Film;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.FilmActor;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Language;
import com.appspot.relaxe.pg.pagila.PagilaPersistenceContext;
import com.appspot.relaxe.rdbms.PersistenceContext;
import com.appspot.relaxe.rdbms.pg.PGImplementation;


public class PagilaPersistenceManagerTest
	extends AbstractPersistenceManagerTest<PGImplementation> {
		
	private UnificationContext unificationContext = new SimpleUnificationContext();
	
    public void testMerge() 
	    throws Exception {
	    
	    Connection c = getConnection();
	    assertFalse(c.getAutoCommit());
	    
	    // PublicFactory pf = new PublicFactoryImpl(); 
	    
	    Actor.Mutable a = newEntity(Actor.Type.TYPE);	            
	    a.setFirstName("Dana");
	    a.setLastName("Brooks");
	
	    PersistenceManager<Actor.Attribute, Actor.Reference, Actor.Type, Actor, Actor.Mutable, Actor.Holder, Actor.Factory, Actor.MetaData> pm = create(a.as());
	    
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
	    
	    final Actor.Mutable a = newEntity(Actor.Type.TYPE);	            
	    a.setFirstName("Dana");
	    a.setLastName("Brooks");
	
	    Film.Mutable f = newEntity(Film.Type.TYPE);	    
	    f.setTitle("New Film");
	    	    
	    Language.Mutable lang = newEntity(Language.Type.TYPE);
	    lang.setName("English");	    	    
	    f.setLanguage(Film.LANGUAGE, lang.ref());
	    
	    FilmActor.Mutable filmActor = newEntity(FilmActor.Type.TYPE);
	    
	    filmActor.setActor(FilmActor.ACTOR, a.ref());
	    filmActor.setFilm(FilmActor.FILM, f.ref());
	    	    	    
	    merge(filmActor.as(), getPersistenceContext(), c);
	    c.commit();    
	    
	    assertTrue(filmActor.isIdentified());
	    delete(filmActor);
	    c.commit();
	    
	    assertTrue(a.isIdentified());
	    
	    Actor sa = sync(a.as(), getPersistenceContext(), c);
	    assertNotNull(sa);
	    Set<Actor.Attribute> ma = sa.getMetaData().attributes();
	    assertEquals(ma, sa.attributes());
	    	    
	    delete(a);
	    c.commit();
	    
	    
	    assertTrue(f.isIdentified());
	    delete(f);
	    c.commit();
	    
	    assertTrue(lang.isIdentified());
	    logger().debug("testMergeDependent: lang.getLanguageId()=" + lang.getLanguageId());
	    
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
		return "pagila";
	}
	
    public void testSync1() 
		throws Exception {
	
	    Connection c = getConnection();
	    PersistenceContext<PGImplementation> pc = getPersistenceContext();
	    	    
	    setUnificationContext(new SimpleUnificationContext());
	    logger().debug("testSync1: getUnificationContext()=" + getUnificationContext());
	    
	    assertFalse(c.getAutoCommit());
	    
	    final Actor.Mutable a = newEntity(Actor.Type.TYPE);
	    Integer id = Integer.valueOf(1);
	    a.setActorId(id);	    
	    assertEquals(a.attributes().size(), 1);
	    
	    Actor sr = sync(a.as(), pc, c);
	    assertNotSame(a, sr);
	    
	    assertEquals(Actor.Attribute.values().length, sr.attributes().size());	    	    
	}		
	
	
    public void testSync() 
		throws Exception {
	
	    Connection c = getConnection();
	    PersistenceContext<PGImplementation> pc = getPersistenceContext();
	    assertFalse(c.getAutoCommit());
	    
	    final Actor.Mutable a = newEntity(Actor.Type.TYPE);	            
	    a.setFirstName("Dana");
	    a.setLastName("Brooks");
	    
	    final Actor ma = merge(a.as(), pc, c);
	    
	    Integer aid = ma.getActorId().value();
	    assertNotNull(aid);
	        	
	    Set<Actor.Attribute> expected = ma.getMetaData().attributes();
	    assertEquals(ma.attributes(), expected);
	    
	    for (Actor.Attribute aa : Actor.Attribute.values()) {
			assertNotNull(aa);
			assertTrue(expected.contains(aa));
		}
	    
	    	    
	    Actor.Mutable pa = newEntity(Actor.Type.TYPE);
	    pa.setActorId(aid);	    
	    assertEquals(pa.attributes().size(), 1);
	    
	    Actor sr = sync(pa.as(), pc, c);	        	        	    
	    assertEquals(sr.attributes(), expected);
	    	    
	}	
}
