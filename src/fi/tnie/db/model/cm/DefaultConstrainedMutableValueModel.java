/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model.cm;

import fi.tnie.db.model.MutableValueModel;

public class DefaultConstrainedMutableValueModel<V>
	extends AbstractConstrainedValueModel<V>
	implements ConstrainedMutableValueModel<V> {

	private V value;
		
	public DefaultConstrainedMutableValueModel(V value) {
		super();
		this.value = value;
	}

	public DefaultConstrainedMutableValueModel() {
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
	protected Proposition createProposition(V newValue, Proposition impliedBy) {
		return new SimpleProposition<V>(this, newValue, impliedBy) {
			@Override
			protected void apply() {
				DefaultConstrainedMutableValueModel<V> m = DefaultConstrainedMutableValueModel.this;
				m.value = to();
				m.fireIfChanged(from(), to());
			}
		};
	}
	
	@Override
	public MutableValueModel<V> asMutable() {	
		return this;
	}
	
	@Override
	public boolean isNullable() {
		return true;
	}
}
