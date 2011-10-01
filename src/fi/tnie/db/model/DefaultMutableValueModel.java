/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public class DefaultMutableValueModel<V>
	extends AbstractMutableValueModel<V> {

	private V value;
	
	public DefaultMutableValueModel() {
		super();
	}

	public DefaultMutableValueModel(V initialValue) {
		super();		
		this.value = initialValue;
	}
	
	@Override
	public V get() {
		return this.value;
	}

	@Override
	public void set(V newValue) {
		V from = this.value;
		this.value = newValue;
		fireIfChanged(from, this.value);
	}
	
}
