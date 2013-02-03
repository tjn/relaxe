/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.util.AbstractList;

public class TestList
	extends AbstractList<String> {
	
	private int size;
	
	public TestList(int size) {
		super();
		this.size = size;
	}

	@Override
	public String get(int index) {
		return Integer.toString(index, 32);
	}

	@Override
	public int size() {
		return this.size;
	}

}
