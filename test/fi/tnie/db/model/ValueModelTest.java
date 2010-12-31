/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

import fi.tnie.db.model.cm.ChangeSet;
import fi.tnie.db.model.cm.ConstrainedIntegerModel;
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
	
	public void testImmutableModel() {
		ConstrainedIntegerModel model = new ConstrainedIntegerModel();		
		assertNotNull(model.asMutable());
		
		ImmutableValueModel<Integer> im = model.asImmutable();		
		assertNotNull(im);
		assertNull(im.asMutable());
		assertNotNull(im.asImmutable());
	}
	
	public void testMutableModel() {
		
		ConstrainedIntegerModel model = new ConstrainedIntegerModel();		
		assertNotNull(model.asMutable());
						
		ImmutableValueModel<Integer> im = model.asMutable().asImmutable();
		assertNotNull(im);
		
		assertNull(model.get());
		assertNull(im.get());
		
		Integer value = 3;
		model.set(value);
		assertEquals(value, model.get()); 
		assertEquals(value, im.get());
		
		ChangeCounter<Integer> cc = new ChangeCounter<Integer>();
		
		Registration r1 = model.addChangeHandler(cc);
		Registration r2 = im.addChangeHandler(cc);
		
		model.set(null);		
		assertEquals(2, cc.getCount());		
		r1.remove();
		model.set(value);		
		assertEquals(3, cc.getCount());		
		r2.remove();
		model.set(null);
		// no mode handlers
		assertEquals(3, cc.getCount());
	}	
}
