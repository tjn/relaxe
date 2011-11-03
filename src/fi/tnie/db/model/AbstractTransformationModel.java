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

	private MutableValueModel<V> result = new DefaultMutableValueModel<V>(null);
	
	public AbstractTransformationModel(final ValueModel<S> source) {
		source.addChangeHandler(new ChangeListener<S>() {
			@Override
			public void changed(S from, S to) {
				result.set(transform(to));
			}
		});
		
		this.result.set(transform(source.get()));
	}
	
	@Override
	public Registration addChangeHandler(ChangeListener<V> ch) {
		return this.result.addChangeHandler(ch);
	}
	
	/**
	 * @param source
	 * @return
	 */
	public abstract V transform(S source);
	
	@Override
	public V get() {		
		return result.get();
	}
	
	@Override
	public MutableValueModel<V> asMutable() {
		return null;
	}
}
