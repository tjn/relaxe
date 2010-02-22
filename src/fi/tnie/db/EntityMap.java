/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.EnumMap;
import java.util.Map;

public class EntityMap<
	K extends Enum<K>, 
	V extends Entity<?, ?, ?>>
	extends EnumMap<K, V> {

	public EntityMap(Class<K> arg0) {
		super(arg0);
	}

	public EntityMap(EnumMap<K, ? extends V> m) {
		super(m);		
	}

	public EntityMap(Map<K, ? extends V> m) {
		super(m);	
	}
}
