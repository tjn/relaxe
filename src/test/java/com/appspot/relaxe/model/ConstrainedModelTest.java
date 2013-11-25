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
