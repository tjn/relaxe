/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import java.io.Serializable;

public class DefaultArray<E extends Serializable>
	implements Serializable, ArrayValue<E> {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4658618167635030919L;
	
	private E[] content = null;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private DefaultArray() {
	}
	
	protected E[] getContent() {
		return content;
	}
	
	public DefaultArray(ArrayValue<E> src) {
		if (src == null) {
			throw new NullPointerException("src");
		}
		
		this.content = src.toArray();
	}
		
	protected DefaultArray(E[] content) {
		if (content == null) {
			throw new NullPointerException("content");
		}
		
		this.content = content; 
	}

	/* (non-Javadoc)
	 * @see fi.tnie.db.rpc.ArrayValue#size()
	 */
	public int size() {
		return this.content.length;
	}
	
	/* (non-Javadoc)
	 * @see fi.tnie.db.rpc.ArrayValue#get(int)
	 */
	public E get(int index) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}
		
		return this.content[index];
	}

	@Override
	public E[] toArray() {
		return content.clone();
	}
}
