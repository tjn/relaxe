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
package com.appspot.relaxe.samples.pagila;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.query.EntityQueryPredicate;
import com.appspot.relaxe.ent.value.Attribute;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Film;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Language;
import com.appspot.relaxe.pg.pagila.PagilaPersistenceContext;
import com.appspot.relaxe.service.DataAccessContext;
import com.appspot.relaxe.service.DataAccessSession;
import com.appspot.relaxe.service.EntitySession;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.StringHolder;
import com.appspot.relaxe.value.ValueHolder;
import com.appspot.relaxe.value.VarcharHolder;

/**
 * A sample program to demonstrate the use of EntitySession
 */
public class PagilaEntitySessionSample {

	public static void main(String[] args) {
		
		DataAccessSession das = null;
		
		try {
			try {
				PagilaPersistenceContext pc = new PagilaPersistenceContext();		
				
				Properties props = pc.getImplementation().getProperties();
				
				String jdbcURL = "jdbc:postgresql:pagila";
				
				props.setProperty("user", "pagila");
				props.setProperty("password", "password");
						
				DataAccessContext dac = pc.newDataAccessContext(jdbcURL, props);
										
				das = dac.newSession();
				
				// EntitySession is one of the three aspects of data access session
				// and the one which allows us to Create/Read/Update/Delete entities: 
				EntitySession es = das.asEntitySession();															
	
				// Creates in-memory entity - DB is not touched yet: 
				Film film = es.newEntity(Film.Type.TYPE);
				
				dumpEntity(film);				
								
				film.set(Film.TITLE, VarcharHolder.valueOf("Terminator"));
				
				dumpEntity(film);
				
				// equivalent to:
				film.setString(Film.TITLE, "Terminator");
				
				// The following won't compile, because 
				// the types of attribute and value do not match:
				// 
				// film.set(Film.FILM_ID, VarcharHolder.valueOf("Terminator"));
				
				// neither compiles this: 
				// film.set(Film.TITLE, IntegerHolder.valueOf(123));
				
								
				// But because entities are sparse, getters return holders, not wrapped values.
				StringHolder<?, ?> holder = film.getTitle();
				
				boolean valueNotPresent = (holder == null);
				System.out.println("value not present ? " + valueNotPresent);
								
				boolean valuePresentButNull = (holder != null) && holder.isNull();
				System.out.println("value present but null ? " + valuePresentButNull);
				
				boolean valuePresentAndNotNull = (holder != null) && (!holder.isNull());
				System.out.println("value present and not null ? " + valuePresentAndNotNull);
												
							
	//			Entities can also manipulated via bean-like interface:				
				film.setTitle("Terminator II");
											
				dumpEntity(film);
							
				Language.QueryElement qe = new Language.QueryElement(
						Language.LANGUAGE_ID,
						Language.NAME
				);
																
				EntityQueryPredicate lp = qe.newEquals(Language.NAME, "English");																
				Language.Query lq = new Language.Query(qe, lp);
														
				// query elements also provide some type-safety - the following would not compile:
//				Language.QueryElement qe = new Language.QueryElement(
//						Film.FILM_ID,
//						Language.NAME
//				);
				
//				The following does not compile either:				
//				EntityQueryPredicate fp = qe.newEquals(Film.TITLE, "English");
												
				List<Language> results = es.list(lq, null);
				
				Language language = results.isEmpty() ? null : results.get(0);
				
				if (language == null) {
					System.out.println("Could not find the language entity: English");
					return;
				}
				
				System.out.println("found the language");
				
				dumpEntity(language);
				
				film.setLanguage(Film.LANGUAGE, language.ref());
																															
				film = es.merge(film);
												
				System.out.println("merged.");
								
				film.setLength(Integer.valueOf(120));
								
				dumpEntity(film);				
								
				das.rollback();
				System.out.println("rolled back.");
			}
			finally {
				// DataAccessSession wraps a connection, 
				// so it's might be a good idea to close it when we're done:				
				if (das != null) {
					das.close();
				}				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	}

	private static  <
		A extends AttributeName,	
		E extends Entity<A, ?, ?, E, ?, ?, M>,		
		M extends EntityMetaData<A, ?, ?, E, ?, ?, M>
	>
	void dumpEntity(E e) {
		M meta = e.getMetaData();
						
		System.out.println(": {");
		
		for (A a : meta.attributes()) {
			Attribute<A, E, ?, ?, ?, ?> key = meta.getKey(a);
			dump(e, key.self());
		}
		
		System.out.println("}");
	}
	
	
	private static <
		A extends AttributeName,	
		E extends Entity<A, ?, ?, E, ?, ?, ?>,
		V extends Serializable,
		P extends ValueType<P>,
		H extends ValueHolder<V, P, H>,	
		K extends Attribute<A, E, V, P, H, K>
	>
	void dump(Entity<A, ?, ?, E, ?, ?, ?> entity, Attribute<A, E, V, P, H, K> key) {		
		H holder = key.get(entity.self());
				
		if (holder == null) {
			System.out.println(key.name() + " is not present");
		}
		else {
			if (holder.isNull()) {
				System.out.println(key.name() + " is null");
			}
			else {
				System.out.println(key.name() + ": " + holder.value());
			}			
		}		
	}
	
}
