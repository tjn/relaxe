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
		set(initialValue);
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
		
	
	public static class NotNullable<V>
		extends DefaultMutableValueModel<V>
		implements NotNullableModel<V> {
		
		public NotNullable(V initialValue) {
			super(initialValue);		
		}

		@Override
		public void set(V value) {
			if (value == null) {
				throw new NullPointerException(this.toString());
			}
			
			super.set(value);
		};
		
		@Override
		public final boolean isNullable() {
			return false;
		}
		
	}
}
