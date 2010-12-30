/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model.cm;

import fi.tnie.db.model.MutableValueModel;

public class ConstrainedMutableValueModel<V>
	extends AbstractConstrainedValueModel<V>
	implements MutableValueModel<V> {

	private V value;
	
	public ConstrainedMutableValueModel(V value) {
		super();
		this.value = value;
	}

	public ConstrainedMutableValueModel() {
		super();
	}

	@Override
	public void set(V newValue) {
		// TODO: silenly fails? should we throw an exception 
		apply(newValue);	
	}

	@Override
	public V get() {
		return this.value;
	}
	
	@Override
	protected Proposition createProposition(V newValue) {
		return new SimpleProposition<V>(this, newValue) {
			@Override
			protected void apply() {
				ConstrainedMutableValueModel<V> m = ConstrainedMutableValueModel.this;
				m.value = to();
				m.fireIfChanged(from(), to());
			}
		};
	}	
}
