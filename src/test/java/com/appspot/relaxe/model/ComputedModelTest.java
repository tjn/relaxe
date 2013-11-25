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
import com.appspot.relaxe.model.cm.ConstrainedComputedModel;
import com.appspot.relaxe.model.cm.ConstrainedIntegerModel;
import com.appspot.relaxe.model.cm.Constraint;
import com.appspot.relaxe.model.cm.Proposition;

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
