/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import junit.framework.TestCase;

public class DefaultListModelTest extends TestCase {

	private static Logger logger = Logger.getLogger(DefaultListModelTest.class);
	
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
