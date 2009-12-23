package fi.tnie.db;

import fi.tnie.db.meta.BaseTable;

public interface EntityFactory<K extends Enum<K>, E extends DBEntity<K, E>> {
	
	BaseTable getTable();
	E newInstance() 
		throws InstantiationException, IllegalAccessException;
	
//	EntityQueryResult<K, E> load(EntityQuery<K, E> request);
	EntityQuery<K, E> createQuery();
	Class<K> getColumnNameType();
}
