/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public abstract class Type<T extends Type<? extends T>> {
	
	public Type() {
		super();
	}
		
	public boolean equals(T t) {
		return (this == t);		
	}
	
	public abstract boolean isReferenceType();
}
