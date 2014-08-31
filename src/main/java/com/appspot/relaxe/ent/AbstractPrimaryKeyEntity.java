package com.appspot.relaxe.ent;

import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;

public abstract class AbstractPrimaryKeyEntity<
	A extends AttributeName,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, B, H, F, M>,
	E extends Entity<A, R, T, E, B, H, F, M>,
	B extends MutableEntity<A, R, T, E, B, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, B, H, M, F>,
	M extends EntityMetaData<A, R, T, E, B, H, F, M>
	> extends DefaultImmutableEntity<A, R, T, E, B, H, F, M> {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -734191010873474350L;
	
	
	@Override
	public E toImmutable(Operation.Context ctx) {
		return self();
	}
	
	
}
