/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.EnumMap;
import java.util.Map;


@SuppressWarnings("serial")
public class ReferenceMap<
	K extends Enum<K>, 
	V extends Entity<?, ?, ?, ?, ?, ?, ?>>
	extends EnumMap<K, V> {

	public ReferenceMap(Class<K> arg0) {
		super(arg0);
	}

	public ReferenceMap(EnumMap<K, ? extends V> m) {
		super(m);		
	}

	public ReferenceMap(Map<K, ? extends V> m) {
		super(m);	
	}
}
