/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.model;

class ChangeCounter<T> 
	implements ChangeListener<T> {

	private int count = 0;
	@Override
	public void changed(T from, T to) {
		count++;
	}
	
	public int getCount() {
		return count;
	}
}