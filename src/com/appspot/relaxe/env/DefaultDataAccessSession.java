/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env;

import java.sql.Connection;
import com.appspot.relaxe.SimpleUnificationContext;
import com.appspot.relaxe.ent.UnificationContext;


public abstract class DefaultDataAccessSession<I extends Implementation<I>>
	extends AbstractDataAccessSession<I>  {
		
	private PersistenceContext<I> persistenceContext;
	private UnificationContext unificationContext;
	
	public DefaultDataAccessSession(PersistenceContext<I> implementation, Connection connection) {
		super(connection);
		this.persistenceContext = implementation;
		this.unificationContext = new SimpleUnificationContext();
	}
	
	@Override
	protected PersistenceContext<I> getPersistenceContext() {
		return this.persistenceContext;
	}
	
	@Override
	protected UnificationContext getUnificationContext() {
		return this.unificationContext;
	}
	
}
