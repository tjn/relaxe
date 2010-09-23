/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.io.Serializable;

/**
 * 
 * @author Topi Nieminen <topi.nieminen@gmail.com>
 */
public abstract class Holder
	implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2127849751940907829L;
	
	public abstract Serializable value();	
	// public abstract int getType();
}
