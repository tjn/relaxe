/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import java.io.Serializable;

public interface Response<R extends Request> 
	extends Serializable {
	
	public R getRequest();
}
