/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import java.io.Serializable;

import com.appspot.relaxe.types.AbstractType;


public interface HolderSource<V extends Serializable, T extends AbstractType<T>, H extends AbstractHolder<V, T, H>> {
	H newHolder();
}
