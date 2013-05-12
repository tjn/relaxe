/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model;


/** 
 *
 * @param <V> AbstractType of the value (computation result) of this model. 
 * @param <S> AbstractType of the input of the computation this model represents.
 */
public abstract class AbstractTransformationModel<V, S>
	extends AbstractValueModel<V> {
	
	 private ValueModel<S> source;
//	private MutableValueModel<V> result;
	
	public AbstractTransformationModel(final ValueModel<S> source) {
		source.addChangeHandler(new ChangeListener<S>() {
			@Override
			public void changed(S from, S to) {
				// change has already taken place:
				V previous = transform(from);	
				V current = transform(to);
				fireIfChanged(previous, current);
			}
		});
		
		this.source = source;
	}
	
	/**
	 * @param source
	 * @return
	 */
	public abstract V transform(S source);

	private ValueModel<S> getSource() {
		return source;
	}
	
	@Override
	public V get() {		
		return transform(getSource().get());
	}
}
