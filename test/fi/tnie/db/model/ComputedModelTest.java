/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

import fi.tnie.db.model.cm.ChangeSet;
import fi.tnie.db.model.cm.ConstrainedComputedModel;
import fi.tnie.db.model.cm.Constraint;
import fi.tnie.db.model.cm.Proposition;
import junit.framework.TestCase;

public class ComputedModelTest extends TestCase {

	public void testComputedModel() {		
		final ConstrainedIntegerModel source = new ConstrainedIntegerModel(); 
		 
		final ConstrainedComputedModel<Boolean, Integer> cm = 
			new ConstrainedComputedModel<Boolean, Integer>(source) {
				@Override
				public Boolean compute(Integer source) {					
					return 
						(source == null) ? null : 
							Boolean.valueOf(source.intValue() % 4 == 0);  
				}			
		};
		
		cm.getSource().addConstraint(new Constraint() {			
			@Override
			public void apply(ChangeSet cs, Proposition p) {
				Integer value = source.proposed(cs);
				
				if (value != null && value.intValue() > 100) {
					p.reject();
				}				 
			}
		});		
		
		assertNull(cm.get());						
		Proposition p;
		
		{
			p = source.apply(Integer.valueOf(1));
			assertNotNull(p);
			assertFalse(p.isRejected());
			assertEquals(Boolean.FALSE, cm.get());
			
			p = source.apply(null);
			assertNotNull(p);
			assertFalse(p.isRejected());
			assertNull(cm.get());
						
			Integer v4 = Integer.valueOf(4);
			p = source.apply(v4);
			assertNotNull(p);
			assertEquals(v4, source.get());
			assertFalse(p.isRejected());
			assertEquals(Boolean.TRUE, cm.get());
						
			Integer v = Integer.valueOf(400);
			p = source.apply(v);			
			assertNotNull(p);
			assertEquals(Boolean.TRUE, cm.get());
			
			assertTrue(p.isRejected());
			assertEquals(Boolean.TRUE, cm.get());						
		}		
	}
}
