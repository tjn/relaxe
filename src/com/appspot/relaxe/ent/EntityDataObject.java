/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

public abstract class EntityDataObject<
	E extends Entity<?, ?, ?, E, ?, ?, ?, ?>	
>
	extends MutableDataObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6947602879806617641L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected EntityDataObject() {
		super();
	}
	
	public EntityDataObject(MetaData m) {
		super(m);
	}

	public abstract E getRoot();
	
}
