/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import java.io.Serializable;

import com.appspot.relaxe.types.AbstractPrimitiveType;


public interface Attribute
	extends Identifiable, Serializable {

	AbstractPrimitiveType<?> type();	
}
