/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import fi.tnie.db.ent.ContextRegistration;
import fi.tnie.db.ent.UnificationContext;

public class SimpleUnificationContext implements UnificationContext {

	private Set<ContextRegistration> registrationSet;
	
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
}
