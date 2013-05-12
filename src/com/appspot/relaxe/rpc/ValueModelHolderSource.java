/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import java.io.Serializable;

import com.appspot.relaxe.model.ValueModel;
import com.appspot.relaxe.types.AbstractType;


public abstract class ValueModelHolderSource<V extends Serializable, T extends AbstractType<T>, H extends AbstractHolder<V, T, H>>
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
