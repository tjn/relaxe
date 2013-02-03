/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.rpc.StringHolder;
import fi.tnie.db.types.PrimitiveType;

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
