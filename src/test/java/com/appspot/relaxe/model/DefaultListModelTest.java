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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.model.ChangeListener;
import com.appspot.relaxe.model.DefaultListModel;
import com.appspot.relaxe.model.ListModel;

import junit.framework.TestCase;

public class DefaultListModelTest extends TestCase {

	private static Logger logger = LoggerFactory.getLogger(DefaultListModelTest.class);
	
	public void testModel() {
		final List<String> history = new ArrayList<String>();		
		ListModel<String> m = new DefaultListModel<String>();
		
		m.addChangeHandler(new ChangeListener<List<String>>() {			
			@Override
			public void changed(List<String> from, List<String> to) {
				history.addAll(to);
			}
		});
		
		List<String> list = m.get();
		assertNotNull(list);
		assertTrue(list.isEmpty());
		assertEquals(0, list.size());
		
		list.add("abc");
		assertEquals(1, history.size());
		assertEquals(history.get(0), "abc");		
		list.remove(0);
		assertEquals(1, history.size());
		assertEquals(history.get(0), "abc");

		list.clear();
		assertEquals(1, history.size());
		assertEquals(history.get(0), "abc");
		
		list.add("abc");
				
		List<String> sl = list.subList(0, list.size());
		
		int size = 0;
		size = sl.size();
		sl.add("def");
		assertEquals(size + 1, sl.size());
	
		size = history.size();
		list.add(0, "head");				
		list.set(0, "HEAD");
		
		list.remove(0);

		logger().debug("list: list=" + list);
		logger().debug("sub-list=" + sl);			
		logger().debug("history=" + history);
		
		m.addChangeHandler(new ChangeListener<List<String>>() {			
			@Override
			public void changed(List<String> from, List<String> to) {
				try {
					from.add("no change");
					throw new RuntimeException();
				}
				catch (UnsupportedOperationException e) {
				}
				try {
					to.clear();
					throw new RuntimeException();
				}
				catch (UnsupportedOperationException e) {
				}
			}
		});
				
		list.add("");
	}	
	
	private static Logger logger() {
		return DefaultListModelTest.logger;
	}
}
