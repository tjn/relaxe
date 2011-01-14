/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;


/** 
 *
 * @param <V> Type of the value (computation result) of this model. 
 * @param <S> Type of the input of the computation this model represents.
 */
public abstract class AbstractTransformationModel<V, S>
	extends AbstractValueModel<V> {
	
	private ValueModel<S> source;
	
	public AbstractTransformationModel(final ValueModel<S> source) {
		source.addChangeHandler(new ChangeListener<S>() {
			@Override
			public void changed(S from, S to) {
				// change has already taken place:
				V previous = transform(from);				
				fireIfChanged(previous, get());
			}
		});
		
		this.source = source;
	}
	
	/**
	 * @param source
	 * @return
	 */
	public abstract V transform(S source);

	public ValueModel<S> getSource() {
		return source;
	}
	
	@Override
	public V get() {		
		return transform(getSource().get());
	}
}
