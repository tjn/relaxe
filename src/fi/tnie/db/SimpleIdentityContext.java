/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.ArrayList;
import java.util.List;

import fi.tnie.db.ent.ContextRegistration;
import fi.tnie.db.ent.IdentityContext;

public class SimpleIdentityContext implements IdentityContext {

	private List<ContextRegistration> registrationList;
	
	@Override
	public void add(ContextRegistration registration) {
		if (registration == null) {
			throw new NullPointerException("registration");
		}	
		
		getRegistrationList().add(registration);
	}

	@Override
	public void close() {
		for (ContextRegistration r : getRegistrationList()) {
			r.remove();
		}		

		getRegistrationList().clear();
	}

	private List<ContextRegistration> getRegistrationList() {
		if (registrationList == null) {
			registrationList = new ArrayList<ContextRegistration>();			
		}

		return registrationList;
	}
}
