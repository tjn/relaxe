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
package com.appspot.relaxe.mysql.sakila;

import java.sql.Connection;
import java.sql.Statement;

import com.appspot.relaxe.AbstractPersistenceManagerTest;
import com.appspot.relaxe.PersistenceManager;
import com.appspot.relaxe.SimpleUnificationContext;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.gen.mysql.sakila.ent.sakila.Actor;
import com.appspot.relaxe.gen.mysql.sakila.ent.sakila.Film;
import com.appspot.relaxe.gen.mysql.sakila.ent.sakila.FilmActor;
import com.appspot.relaxe.gen.mysql.sakila.ent.sakila.Language;
import com.appspot.relaxe.gen.mysql.sakila.ent.sakila.Actor.Factory;
import com.appspot.relaxe.gen.mysql.sakila.ent.sakila.Actor.Holder;
import com.appspot.relaxe.gen.mysql.sakila.ent.sakila.Actor.MetaData;
import com.appspot.relaxe.gen.mysql.sakila.ent.sakila.Actor.Reference;
import com.appspot.relaxe.gen.mysql.sakila.ent.sakila.Actor.Type;
import com.appspot.relaxe.rdbms.PersistenceContext;
import com.appspot.relaxe.rdbms.mysql.MySQLImplementation;


public class SakilaPersistenceManagerTest
	extends AbstractPersistenceManagerTest<MySQLImplementation> {
		
	private UnificationContext unificationContext = new SimpleUnificationContext();

    public void testMerge() 
    	throws Exception {
    
	    Connection c = getConnection();
	    
	    Statement st = c.createStatement();
	    st.executeUpdate("SET sql_mode = ANSI");
	    st.close();	    
	    	    	    
	    assertFalse(c.getAutoCommit());
	    
	    // SakilaFactory sf = new SakilaFactoryImpl(); 
	    
	    Actor.Mutable a = newEntity(Actor.Type.TYPE);	            
	    a.setFirstName("Dana");
	    a.setLastName("Brooks");
	
	    PersistenceManager<Actor.Attribute, Reference, Type, Actor, Actor.Mutable, Holder, Factory, MetaData> pm = create(a.as());
	    
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
	            
	    Actor.Mutable a = newEntity(Actor.Type.TYPE);	            
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
	    delete(a);
	    c.commit();
	    
	    assertTrue(f.isIdentified());
	    delete(f);
	    c.commit();
	    
	    assertTrue(lang.isIdentified());
	    logger().debug("testMergeDependent: lang.getContent().getLanguageId()=" + lang.getLanguageId());
	    
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
