/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.util.Map;

public interface Catalog
	extends MetaObject {
	
	Map<String, Schema> schemas();	
}
