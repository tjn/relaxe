/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model.ent;

import java.io.Serializable;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.ent.value.PrimitiveKey;
import com.appspot.relaxe.model.ValueModel;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.PrimitiveType;
import com.appspot.relaxe.types.ReferenceType;


public interface AttributeModelMap<
	A extends Attribute,
	V extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<V, P, H>,
	T extends ReferenceType<A, ?, T, E, ?, ?, ?, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?, ?>,
	D extends AttributeModelMap<A, V, P, H, T, E, D>
>	
{	
	D self();
	
	<			
		K extends PrimitiveKey<A, E, V, P, H, K>
	>	
	ValueModel<H> attr(K k) throws EntityRuntimeException;		
}
