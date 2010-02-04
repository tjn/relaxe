/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Collections;
import java.util.List;


public class RowQueryResult<C extends Enum<C>, E extends Row<C>> {		

	private List<E> content;
	private RowQuery<C, E> source;
	private int available;
	
	public RowQueryResult(RowQuery<C, E> source, List<E> content, int available) {		
		super();
		
		if (source == null) {
			throw new NullPointerException("'source' must not be null");
		}
		
		this.source = source;
		this.content = content;
		this.available = available;
	}

	public List<E> getContent() {
		if (content == null) {
			return Collections.emptyList();
		}
		
		return Collections.unmodifiableList(this.content);
	}

	public RowQuery<C, E> getSource() {
		return source;
	}

	public int getAvailable() {
		return available;
	}

	public E first() {
		return (available > 0) ? this.content.get(0) : null;
	}

}
