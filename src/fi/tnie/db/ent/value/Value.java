/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;

public interface Value<
	A extends Serializable, 
	S extends Serializable, 
	P extends PrimitiveType<P>, 
	H extends PrimitiveHolder<S, P>,
	E extends Entity<A, ?, ?, E>,
	K extends Key<A, S, P, H, E, K>	
> 
	extends Serializable
{		
	K key();	
	void set(S newValue);	
	S get();	
	void setHolder(H newHolder);	
	public H getHolder();
}
