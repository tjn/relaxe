/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import java.io.Serializable;

import com.appspot.relaxe.expr.Identifier;


public interface MetaObject
	extends Serializable {
	public Identifier getUnqualifiedName();
	
}
