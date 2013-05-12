/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model.cm;

import com.appspot.relaxe.model.ChangeListener;

/** 
 * @param <V> AbstractType of the value (computation result) of this model. 
 * @param <S> AbstractType of the input of the computation this model represents.
 */
public abstract class ConstrainedComputedModel<V, S>
	extends AbstractConstrainedValueModel<V> {
	
	private ConstrainedValueModel<S> source;
	
	public ConstrainedComputedModel(final ConstrainedValueModel<S> source) {
		source.addConstraint(new Constraint() {
			@Override
			public void apply(ChangeSet cs, Proposition change) {
				S proposed = source.proposed(cs);				
				V result = compute(proposed);								
				// rejection of implied proposition also rejects 'p'
				cs.submit(ConstrainedComputedModel.this, result, change);
			}
		});
		
		source.addChangeHandler(new ChangeListener<S>() {
			@Override
			public void changed(S from, S to) {
				fireIfChanged(get(), compute(to));
			}			
		});
		
		this.source = source;
	}
	
	/**
	 * 
	 * @param source
	 * @return
	 */
	public abstract V compute(S source);

	@Override
	public V get() {
		return compute(this.source.get());
	}

	public ConstrainedValueModel<S> getSource() {
		return source;
	}
}
