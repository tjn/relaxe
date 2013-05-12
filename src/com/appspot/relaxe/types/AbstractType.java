/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.types;

import java.io.Serializable;

public abstract class AbstractType<
	T extends Type<T>
>
	implements Serializable, Type<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8522112079681061065L;

	public AbstractType() {
		super();
	}
		
	@Override
	public boolean equals(T t) {
		return (this == t);		
	}
	
	/* (non-Javadoc)
	 * @see com.appspot.relaxe.types.Type#isReferenceType()
	 */
	@Override
	public abstract boolean isReferenceType();
}
