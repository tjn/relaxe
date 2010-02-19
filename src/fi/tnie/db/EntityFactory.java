/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public interface EntityFactory<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,	
	E extends Entity<A, R>	
> {			
//	EntityMetaData<A, R, ?> getMetaData();
	
	E newInstance() 
		throws InstantiationException, IllegalAccessException;

//	EntityQuery<A, E> createEntityQuery();

	//	EnumMap<A, Integer> keys(ResultSetMetaData rs) 
//		throws SQLException;
	
		
//	/**
//	 * Reloads the entity by using a  
//	 * 
//	 * @param predicate
//	 * @return
//	 */
//	E reload(AbstractEntity<A, E> pk, Connection c)
//		throws SQLException;
}
