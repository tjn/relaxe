/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public class ConstantValueModel<V>
	extends AbstractValueModel<V> {
	
	private V value;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected ConstantValueModel() {
	}
	
	public ConstantValueModel(V value) {
		super();
		this.value = value;
	}

	private static final Registration registration = new Registration() {		
		@Override
		public void remove() {			
		}
	};
	
	@Override
	public final Registration addChangeHandler(ChangeListener<V> ch) {
		return registration;
	}

	@Override
	public final V get() {
		return this.value;
	}

}
