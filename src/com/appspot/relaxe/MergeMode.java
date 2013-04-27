/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.Connection;

/**
 * Specifies a behavior for merging the dependencies of the entity currently being merged.  
 * 
 * @see PersistenceManager#merge(Connection)
 * @author tnie
 */    
public enum MergeMode {
	/** 
	 * Only the unidentified (non-null) entities the target refers to are merged.
	 */    	
	UNIDENTIFIED,
	/**
	 * All the non-null entities the target refers to are also merged.
	 */
	ALL
}