/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Collections;
import java.util.List;


public class EntityQueryResult<K extends Enum<K>, E extends Entity<K, E>> {		

	private List<E> content;
	private EntityQuery<K, E> source;
	private int available;
	
	public EntityQueryResult(EntityQuery<K, E> source, List<E> content, int available) {		
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

	public EntityQuery<K, E> getSource() {
		return source;
	}

	public int getAvailable() {
		return available;
	}

	public E first() {
		return (available > 0) ? this.content.get(0) : null;
	}

}
