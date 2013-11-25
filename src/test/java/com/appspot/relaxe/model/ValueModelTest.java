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

import com.appspot.relaxe.model.ImmutableValueModel;
import com.appspot.relaxe.model.Registration;
import com.appspot.relaxe.model.cm.ChangeSet;
import com.appspot.relaxe.model.cm.ConstrainedIntegerModel;
import com.appspot.relaxe.model.cm.Constraint;
import com.appspot.relaxe.model.cm.DefaultConstrainedMutableValueModel;
import com.appspot.relaxe.model.cm.Proposition;

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
