/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import java.io.Serializable;

import com.appspot.relaxe.types.Type;


public interface HolderFactory<V extends Serializable, T extends Type<T>, H extends Holder<V, T>> {
	H newHolder(V value);
		 
}
