/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import java.io.Serializable;

import fi.tnie.db.model.ValueModel;
import fi.tnie.db.types.Type;

public abstract class ValueModelHolderSource<V extends Serializable, T extends Type<T>, H extends Holder<V,T>>
	implements HolderSource<V, T, H> {
	
	private ValueModel<V> model;	
	private HolderFactory<V, T, H> factory;
		
	public ValueModelHolderSource(ValueModel<V> model, HolderFactory<V, T, H> factory) {
		super();
		this.model = model;
		this.factory = factory;
	}

	@Override
	public H newHolder() {
		return factory.newHolder(model.get());
	}
}
