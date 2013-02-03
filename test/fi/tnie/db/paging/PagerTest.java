/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import junit.framework.TestCase;

public class PagerTest extends TestCase {
	
	private TestList testList = new TestList(100);	

	public void testConstructor() {		
		try {
			new StringListPager(new StringListFetcher(testList), 0);
			throw new RuntimeException("expect exception");
		}
		catch (IllegalArgumentException e) {
			
		}
			
		StringListPager p = new StringListPager(new StringListFetcher(testList), 7);
		assertEquals(7, p.getPageSize());
		
		assertNotNull(p.getState());
		assertSame(Pager.State.IDLE, p.getState());
		
		assertNull(p.getCurrentPage());
		assertNull(p.get());		
	}

	private void testPages(int size, int pageSize) {
				
		StringListPager p = new StringListPager(new StringListFetcher(new TestList(size)), pageSize);
				
		boolean fp = p.firstPage();
		assertTrue(fp);
		assertNotNull(p.getCurrentPage());
		assertNotNull(p.get());
		assertEquals("0", p.get());
		
		assertFalse(p.previous());		
		assertFalse(p.previousPage());
		
		assertTrue(p.next());
		assertEquals("1", p.get());
		assertTrue(p.previous());
		assertFalse(p.previous());
		
		
		if (pageSize > 3 && size > 3) {
			assertTrue(p.select(3));
			assertEquals("3", p.get());
		}
		
		assertTrue(p.select(0));
		assertEquals("0", p.get());
		
		if (size > pageSize) {		
			assertTrue(p.nextPage());
			assertEquals(value(pageSize), p.get());
	
			assertTrue(p.previous());
			assertEquals(value(pageSize - 1), p.get());
	
			assertTrue(p.next());
			assertEquals(value(pageSize), p.get());
		}
		else {
			// OK, but loaded page is empty
			assertTrue(p.nextPage());		
			assertNotNull(p.getCurrentPage());
			assertNotNull(p.getCurrentPage().getContent());
			assertTrue(p.getCurrentPage().getContent().isEmpty());
			assertFalse(p.select(size - 1));
			assertFalse(p.next());
		}
	}
	
	public void testManyPages() {
		testPages(100, 7);
	}
	
	public void testHalfEmptyPage() {				
		testPages(5, 10);
	}	

	private String value(int index) {
		return Integer.toString(index, 32);
	}

}
