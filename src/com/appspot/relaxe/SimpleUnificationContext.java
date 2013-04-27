/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.appspot.relaxe.ent.ContextRegistration;
import com.appspot.relaxe.ent.UnificationContext;


public class SimpleUnificationContext implements UnificationContext {

	private Set<ContextRegistration> registrationSet;
	
	// private Map<EntityMetaData<?, ?, ?, ?, ?, ?, ?, ?>, EntityIdentityMap<Attribute, Reference, ReferenceType<A,R,T,E,H,?,M,?>, Entity<A,R,T,E,H,?,M,?>, ReferenceHolder<A,R,T,E,H,M,?>, EntityMetaData<A,R,T,E,H,?,M,?>>> identityMap;
	
	private static Logger logger = Logger.getLogger(SimpleUnificationContext.class);
	
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
	
	
