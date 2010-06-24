/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.meta.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

class ConcatIterator<T>
	implements Iterator<T> {
	
	/**
	 * 
	 */
	private Iterator<? extends T> head;
	private Iterator<? extends T> tail;
			
	ConcatIterator(Iterator<? extends T> head, Iterator<? extends T> tail) {
		super();
		
		if (head == null || tail == null) {
			throw new NullPointerException();
		}
					
		this.head = head;
		this.tail = tail;			
	}

	@Override
	public boolean hasNext() {			
		return head.hasNext() || tail.hasNext();
	}

	@Override
	public T next()
		throws NoSuchElementException {						
		Iterator<? extends T> current = head.hasNext() ? head : tail;			
		return current.next();
	}

	@Override
	public void remove() 
		throws UnsupportedOperationException {
		throw new UnsupportedOperationException("remove is not supported");
	}
	
}