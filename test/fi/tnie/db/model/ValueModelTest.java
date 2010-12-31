/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

import fi.tnie.db.model.cm.ChangeSet;
import fi.tnie.db.model.cm.DefaultConstrainedMutableValueModel;
import fi.tnie.db.model.cm.Constraint;
import fi.tnie.db.model.cm.Proposition;
import junit.framework.TestCase;

public class ValueModelTest extends TestCase {

	
	public void testValueModel() {		
		final DefaultConstrainedMutableValueModel<Boolean> m = 
			new DefaultConstrainedMutableValueModel<Boolean>();
		
		assertNull(m.get());				
		m.set(Boolean.TRUE);
		assertEquals(Boolean.TRUE, m.get());
		m.set(Boolean.FALSE);
		assertEquals(Boolean.FALSE, m.get());
		m.set(null);
		assertNull(m.get());
				
		m.addConstraint(new Constraint() {
			@Override
			public void apply(ChangeSet cs, Proposition p) {
				Boolean pv = m.proposed(cs);
				
				if (pv == null) {
					p.reject();
				}			
			}
		});		
		
		m.set(Boolean.TRUE);
		assertEquals(Boolean.TRUE, m.get());
		
		m.set(null); // should be rejected
		assertEquals(Boolean.TRUE, m.get());
		
		m.set(Boolean.FALSE);
		assertEquals(Boolean.FALSE, m.get());		
	}	
}
