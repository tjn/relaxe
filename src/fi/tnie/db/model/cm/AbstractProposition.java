/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.model.cm;

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
	
	public boolean isRejected() {
		return this.rejectCount > 0;
	}
		
	public boolean isCommitted() {
		return committed;
	}
	
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