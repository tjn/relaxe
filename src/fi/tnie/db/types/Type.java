/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

import java.io.Serializable;

public abstract class Type<T extends Type<? extends T>>
	implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8522112079681061065L;

	public Type() {
		super();
	}
		
	public boolean equals(T t) {
		return (this == t);		
	}
	
	public abstract boolean isReferenceType();
}
