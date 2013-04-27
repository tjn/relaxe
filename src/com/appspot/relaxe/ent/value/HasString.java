/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.StringHolder;
import com.appspot.relaxe.types.PrimitiveType;

public interface HasString<
	A extends Attribute,
	E extends HasString<A, E>> {

	<
		T extends PrimitiveType<T>, 
		H extends StringHolder<T, H>,
		K extends StringKey<A, E, T, H, K>
	>	
	H getString(K key);
	
	<
		T extends PrimitiveType<T>, 
		H extends StringHolder<T, H>,
		K extends StringKey<A, E, T, H, K>
	>
	void setString(K key, H newValue);

}
