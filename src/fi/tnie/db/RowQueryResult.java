/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Collections;
import java.util.List;


public class RowQueryResult<R extends Row> {		

	private List<R> content;
	private RowQuery<R, ?> source;
	private int available;
	
	public RowQueryResult(RowQuery<R, ?> source, List<R> content, int available) {		
		super();
		
		if (source == null) {
			throw new NullPointerException("'source' must not be null");
		}
		
		this.source = source;
		this.content = content;
		this.available = available;
	}

	public List<R> getContent() {
		if (content == null) {
			return Collections.emptyList();
		}
		
		return Collections.unmodifiableList(this.content);
	}

	public RowQuery<R, ?> getSource() {
		return source;
	}

	public int getAvailable() {
		return available;
	}

	public R first() {
		return (available > 0) ? this.content.get(0) : null;
	}

}
