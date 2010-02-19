/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.HashMap;
import java.util.Map;

import fi.tnie.db.meta.BaseTable;

public class DefaultEntityContext
	implements EntityContext {
		
	private Map<BaseTable, EntityMetaData<?, ?, ?>> metaMap;
	
	@Override
	public EntityMetaData<?, ?, ?> getMetaData(BaseTable table) {					
		return getMetaMap().get(table);
	}	
	
	public void register(EntityMetaData<?, ?, ?> meta) {
		getMetaMap().put(meta.getBaseTable(), meta);
	}


	private Map<BaseTable, EntityMetaData<?, ?, ?>> getMetaMap() {
		if (metaMap == null) {
			metaMap = new HashMap<BaseTable, EntityMetaData<?, ?, ?>>();
		}

		return metaMap;
	}	
	
	
}
