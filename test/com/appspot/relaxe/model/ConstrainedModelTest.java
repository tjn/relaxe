/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model;

import com.appspot.relaxe.model.cm.ChangeSet;
import com.appspot.relaxe.model.cm.ConstrainedBinaryOperatorModel;
import com.appspot.relaxe.model.cm.ConstrainedMutableValueModel;
import com.appspot.relaxe.model.cm.DefaultConstrainedMutableValueModel;
import com.appspot.relaxe.model.cm.Proposition;

import junit.framework.TestCase;

public class ConstrainedModelTest extends TestCase {

	public void testConstrainedModel() {		
		ConstrainedMutableValueModel<Boolean> a = 
			new DefaultConstrainedMutableValueModel<Boolean>();
		
		ConstrainedMutableValueModel<Boolean> b = 
			new DefaultConstrainedMutableValueModel<Boolean>();
		 
		ConstrainedBinaryOperatorModel<Boolean, Boolean, Boolean> op = 
			new ConstrainedBinaryOperatorModel<Boolean, Boolean, Boolean>(a, b) {
			
				@Override
				public Boolean compute(Boolean a, Boolean b) {
					Boolean v =  
						(a == null) ? null :  
						(b == null) ? null :
						Boolean.valueOf(a.booleanValue() ^ b.booleanValue());
					
					return v;
				}

				@Override
				public Proposition propose(ChangeSet cs, Boolean newValue, Proposition impliedBy) {
					Proposition p = super.propose(cs, newValue, impliedBy);
					
					if (newValue != null && (!newValue.booleanValue())) {
						p.reject();
					}
					
					return p;
				}
		};
		
		boolean committed;
		
		ChangeCounter<Boolean> cc = new ChangeCounter<Boolean>();		
		op.addChangeHandler(cc);		

		{
			ChangeSet cs = new ChangeSet();		
			cs.add(a, Boolean.TRUE);
			cs.add(b, Boolean.TRUE);	
						
			committed = cs.apply();	
			assertFalse(committed);
			assertEquals(0, cc.getCount());
			assertNull(op.get());
		}

		{
			ChangeSet cs = new ChangeSet();		
			cs.add(a, Boolean.FALSE);
			cs.add(b, Boolean.TRUE);
			
			committed = cs.apply();	
			assertTrue(committed);
			assertEquals(1, cc.getCount());
			assertEquals(Boolean.TRUE, op.get());
		}
		
		{
			ChangeSet cs = new ChangeSet();		
			cs.add(a, Boolean.FALSE);
			cs.add(b, Boolean.FALSE);
			committed = cs.apply();	
			assertFalse(committed);
			
			// should have previous value:
			assertEquals(1, cc.getCount());
			assertEquals(Boolean.TRUE, op.get());
		}
		
		{
			ChangeSet cs = new ChangeSet();		
			cs.add(a, Boolean.TRUE);
			cs.add(b, Boolean.FALSE);
			committed = cs.apply();	
			assertTrue(committed);			
			
			// computed value did not changed:
			assertEquals(1, cc.getCount());
			assertEquals(Boolean.TRUE, op.get());
		}
		
		{
			ChangeSet cs = new ChangeSet();		
			cs.add(a, null);
			cs.add(b, Boolean.FALSE);
			committed = cs.apply();	
			assertTrue(committed);

			// computed value changed:
			assertEquals(2, cc.getCount());
			assertNull(op.get());
		}		
	}	
}