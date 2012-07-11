/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Map;

public class Counter {
	private int value;

	public Counter() {
		super();	
	}
	
	public int increment() {
		return (++value);
	}
	
	public int value() {
		return value;
	}

	public static <K> Counter get(K key, Map<K, Counter> map) {		
		Counter c = map.get(key);
		
		if (c == null) {
			map.put(key, c = new Counter());
		}
		
		return c;
	}
}
