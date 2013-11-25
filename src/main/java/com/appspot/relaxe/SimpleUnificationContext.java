/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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
package com.appspot.relaxe;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.ent.ContextRegistration;
import com.appspot.relaxe.ent.UnificationContext;


public class SimpleUnificationContext implements UnificationContext {

	private Set<ContextRegistration> registrationSet;
	
	// private Map<EntityMetaData<?, ?, ?, ?, ?, ?, ?, ?>, EntityIdentityMap<Attribute, Reference, ReferenceType<A,R,T,E,H,?,M,?>, Entity<A,R,T,E,H,?,M,?>, ReferenceHolder<A,R,T,E,H,M,?>, EntityMetaData<A,R,T,E,H,?,M,?>>> identityMap;
	
	private static Logger logger = LoggerFactory.getLogger(SimpleUnificationContext.class);
	
	@Override
	public void add(ContextRegistration registration) {
		if (registration == null) {
			throw new NullPointerException("registration");
		}	
		
		getRegistrationSet().add(registration);
	}

	@Override
	public void close() {
		for (ContextRegistration r : getRegistrationSet()) {
			r.remove();
		}		

		getRegistrationSet().clear();
	}

	private Collection<ContextRegistration> getRegistrationSet() {
		if (registrationSet == null) {
			registrationSet = new HashSet<ContextRegistration>();			
		}

		return registrationSet;
	}
		
	
//	private Buffer<ReferenceHolder<?, ?, ?, ?, ?, ?, ?>> result = new Buffer<ReferenceHolder<?, ?, ?, ?, ?, ?, ?>>();
	
	private static Logger logger() {
		return SimpleUnificationContext.logger;
	}
	
//	public <		
//		T extends ReferenceType<?, ?, T, E, H, ?, M, ?>,
//		E extends Entity<?, ?, T, E, H, ?, M, ?>,		
//		H extends ReferenceHolder<?, ?, T, E, H, M, ?>, 
//		M extends EntityMetaData<?, ?, T, E, H, ?, M, ?>
//	>	
//	H unify(E e) {
//		return e.getMetaData().unify(this, e);
//	}

}	
	
	
