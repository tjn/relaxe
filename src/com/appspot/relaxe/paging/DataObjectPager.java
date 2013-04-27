/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

import java.io.Serializable;

import com.appspot.relaxe.ent.DataObject;


public interface DataObjectPager<
	O extends DataObject, 
	R extends Serializable & HasDataObjectQueryResult<O>, 
	P extends DataObjectPager<O, R, P>
>
	extends SimplePagerModel<R, P> {
	
	
}
