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
	private boolean submitted;
				
	public AbstractProposition() {
		super();		
//		System.err.println(this);		
	}

	@Override
	public void reject() {
		// new Exception("").printStackTrace(System.err);		
		this.rejectCount++;
	}
	
	public boolean isRejected() {
		
		
		return this.rejectCount > 0;
	}
	
	public boolean isSubmitted() {		
		return submitted;
	}
	
	public boolean isCommitted() {
		// TODO: track explicitly
		return submitted && (!isRejected());
	}
	
	public void commit()
		throws IllegalStateException {
		
		if (isRejected()) {
			throw new IllegalStateException("can not commit rejected proposition");
		}
		
		if (!isSubmitted()) {
			System.err.println(this);
			throw new IllegalStateException("can not commit unsubmitted proposition");
		}
		
		apply();
	}
	
	@Override
	public void submit(ChangeSet cs) {
		this.rejectCount = 0;
		this.submitted = true;
	}
		
	public boolean isCompleted() {
		return isRejected() || isCommitted();
	}

	protected abstract void apply();

}