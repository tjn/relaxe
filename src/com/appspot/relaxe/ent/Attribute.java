/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import java.io.Serializable;

import com.appspot.relaxe.types.PrimitiveType;


public interface Attribute
	extends Identifiable, Serializable {

	PrimitiveType<?> type();	
}
