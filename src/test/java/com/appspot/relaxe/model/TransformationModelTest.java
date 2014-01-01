/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
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

import com.appspot.relaxe.model.AbstractTransformationModel;
import com.appspot.relaxe.model.MutableStringModel;

import junit.framework.TestCase;

public class TransformationModelTest extends TestCase {

	public void testComputedModel() {		
		final MutableStringModel source = new MutableStringModel(null); 
		 
		final AbstractTransformationModel<Integer, String> cm =
			new AbstractTransformationModel<Integer, String>(source) {

				@Override
				public Integer transform(String source) {									
					return (source == null) ? null : Integer.valueOf(source.length());
				}
		};
		
		ChangeCounter<Integer> cc = new ChangeCounter<Integer>();				
		cm.addChangeHandler(cc);
		
		assertNull(cm.get());
		
		String input;		
	
		input = "";
		source.set(input);
		assertNotNull(cm.get());		
		assertEquals(input.length(), cm.get().intValue());
		assertEquals(1, cc.getCount());

		input = "abc"; 
		source.set(input);
		assertNotNull(cm.get());
		assertEquals(input.length(), cm.get().intValue());
		assertEquals(2, cc.getCount());

		input = "bac"; 
		source.set(input);
		assertNotNull(cm.get());
		assertEquals(input.length(), cm.get().intValue());
		// length did not changed
		assertEquals(2, cc.getCount());
	}	
}
