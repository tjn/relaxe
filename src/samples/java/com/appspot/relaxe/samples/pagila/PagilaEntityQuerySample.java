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
import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.ent.query.EntityQueryPredicate;
import com.appspot.relaxe.ent.value.ValueAttribute;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Film;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Language;
import com.appspot.relaxe.pg.pagila.PagilaPersistenceContext;
import com.appspot.relaxe.service.DataAccessContext;
import com.appspot.relaxe.service.DataAccessSession;
import com.appspot.relaxe.service.EntitySession;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.ValueHolder;

/**
 * A sample program to demonstrate the use of EntitySession
 */
public class PagilaEntityQuerySample {

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
				
				EntitySession es = das.asEntitySession();
				
				Film.QueryElement.Builder qb = new Film.QueryElement.Builder();
				
				qb.add(Film.FILM_ID);
				qb.add(Film.TITLE);
				qb.add(Film.RELEASE_YEAR);
				
				Language.QueryElement qe = new Language.QueryElement(
						Language.LANGUAGE_ID,
						Language.NAME
				);
				
				// resulting query will have JOIN: 
				qb.setQueryElement(Film.LANGUAGE, qe);
				
				EntityQueryPredicate lp = qe.newEquals(Language.NAME, "English");
				
				Film.QueryElement fqe = qb.newQueryElement();
							
				Film.Query q = new Film.Query(fqe, lp);
								
				FetchOptions opts = new FetchOptions(3, false);
								
				List<Film> results = es.list(q, opts);
				
				for (Film film : results) {
					dumpEntity(film);
															
					System.out.println("language referenced by film: " + film.getTitle().value());
					
					Language.Holder lh = film.getLanguage(Film.LANGUAGE);
					Language language = lh.value();
					
					dumpEntity(language);
					
					Language.Holder ol = film.getLanguage(Film.ORIGINAL_LANGUAGE);
					
					System.out.println(
							"original language holder is null, " + 
							"because it was not referenced in the query ? " + (ol == null));					
				}
				
				das.rollback();
				System.out.println("rolled back.");
			}
			finally {
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
			ValueAttribute<A, E, ?, ?, ?, ?> key = meta.getKey(a);
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
		K extends ValueAttribute<A, E, V, P, H, K>
	>
	void dump(Entity<A, ?, ?, E, ?, ?, ?> entity, ValueAttribute<A, E, V, P, H, K> key) {		
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
