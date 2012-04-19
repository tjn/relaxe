/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

public class MutableEntityDataObject<
	E extends Entity<?, ?, ?, E, ?, ?, ?, ?>	
>
	extends EntityDataObject<E> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6686405887192221961L;
	private E root;
		
	protected MutableEntityDataObject() {
		super();	
	}

	public MutableEntityDataObject(MetaData m) {
		super(m);
	}

	@Override
	public E getRoot() {
		return root;
	}

	public void setRoot(E root) {
		this.root = root;
	}	
}
