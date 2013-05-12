/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.model.cm;

public abstract class AbstractProposition	
	implements Proposition {
	
	private int rejectCount;
	private boolean committed;
				
	public AbstractProposition() {
		super();		
//		System.err.println(this);		
	}

	@Override
	public void reject() {		
		// new Exception("").printStackTrace(System.err);		
		this.rejectCount++;
		
		Proposition ip = impliedBy();
		
		if (ip != null) {
			ip.reject();
		}		
	}
	
	@Override
	public boolean isRejected() {
		return this.rejectCount > 0;
	}
		
	@Override
	public boolean isCommitted() {
		return committed;
	}
	
	@Override
	public void commit()
		throws IllegalStateException {
		
		if (isRejected()) {
			throw new IllegalStateException("can not commit rejected proposition");
		}
		
		apply();
		this.committed = true;
	}
	
	public boolean isCompleted() {
		return isRejected() || isCommitted();
	}

	protected abstract void apply();
}