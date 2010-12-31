/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model.cm;

import fi.tnie.db.model.ChangeListener;

/** 
 * @param <V> Type of the value (computation result) of this model. 
 * @param <S> Type of the input of the computation this model represents.
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
								
				Proposition np = cs.submit(ConstrainedComputedModel.this, result, change);
				
				if (np.isRejected()) {
					// reject the dependent model
					change.reject();
				}
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
