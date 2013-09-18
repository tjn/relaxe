/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;



//import org.apache.log4j.Logger;

import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;


public abstract class DefaultEntityQueryBuilder<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content,	
	RE extends EntityQueryElement<A, R, T, E, H, F, M, C, RE>
>
	implements EntityQuery.Builder<A, R, T, E, H, F, M, C, RE> {
	

	private RE root;

	public DefaultEntityQueryBuilder(RE root) {
		this.root = root;
	}

	public M getMetaData() {
		return this.root.getMetaData();
	}
	
	@Override
	public EntityQuery<A, R, T, E, H, F, M, C, RE> newQuery() {
		
		return null;
	}
		
}