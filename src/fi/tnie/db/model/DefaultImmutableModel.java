/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public class DefaultImmutableModel<V>
	implements ImmutableValueModel<V> {
	
	private ValueModel<V> inner;

	public DefaultImmutableModel(ValueModel<V> inner) {
		super();
		
		if (inner == null) {
			throw new NullPointerException("wrapped model");
		}
		
		this.inner = inner;
	}

	@Override
	public Registration addChangeHandler(ChangeListener<V> ch) {
		return this.inner.addChangeHandler(ch);
	}

	@Override
	public final MutableValueModel<V> asMutable() {
		return null;		
	}

	@Override
	public V get() {
		return this.inner.get();
	}
	
	@Override
	public ImmutableValueModel<V> asImmutable() {	
		return this;
	}
}
