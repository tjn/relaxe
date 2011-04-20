/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public interface ReferenceWriter<
	A extends Attribute,
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>,
	
	
	V extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<V, P>,
	K extends PrimitiveKey<A, R, T, E, V, P, H, K>> {
	
	public H write(DataObject src, E dest);
}
