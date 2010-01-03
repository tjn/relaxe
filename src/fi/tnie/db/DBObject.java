/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fi.tnie.db.meta.Table;

public class DBObject<K> {
	
	private Date loadedAt;	
	private Map<K, Object> values;
		
	public interface MetaData {
		Table getTable();
	}
	
	protected Map<K, Object> createValueMap() {
		return new HashMap<K, Object>();		
	}
	
	public Map<K, Object> values() {
		if (this.values == null) {
			this.values = createValueMap();		
		}
		
		return Collections.unmodifiableMap(values);
	}
	

}
