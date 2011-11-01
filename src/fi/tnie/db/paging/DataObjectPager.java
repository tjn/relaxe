/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.io.Serializable;

import fi.tnie.db.ent.DataObject;

public interface DataObjectPager<
	O extends DataObject, 
	R extends Serializable & HasDataObjectQueryResult<O>, 
	P extends DataObjectPager<O, R, P>
>
	extends SimplePager<R, P> {
	
	
}
