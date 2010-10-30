/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import java.io.Serializable;

import fi.tnie.db.types.Type;

/**
 * 
 * @author Topi Nieminen <topi.nieminen@gmail.com>
 */
public abstract class Holder<V extends Serializable, T extends Type<T>>
	implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2127849751940907829L;	
	public abstract V value();
	
	public boolean isNull() {
		return value() == null;
	}
	
	public abstract T getType();
}
