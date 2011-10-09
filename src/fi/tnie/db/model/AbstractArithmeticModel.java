/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public abstract class AbstractArithmeticModel<N extends Number, A extends Number, B extends Number>
	implements ImmutableValueModel<N> {

	private MutableValueModel<N> result = new DefaultMutableValueModel<N>(null);

	public AbstractArithmeticModel(final ValueModel<A> a, final ValueModel<B> b) {
		if (!a.isConstant()) {						
			a.addChangeHandler(new ChangeListener<A>() {
				@Override
				public void changed(A from, A to) {	
					compute(a, b);
				}
			});
		}
		
		if (!b.isConstant()) {
			b.addChangeHandler(new ChangeListener<B>() {
				@Override
				public void changed(B from, B to) {	
					compute(a, b);
				}
			});
		}
		
		this.result.set(compute(a.get(), b.get()));
	}
	
	@Override
	public Registration addChangeHandler(ChangeListener<N> ch) {
		return this.result.addChangeHandler(ch);
	}

	protected abstract N compute(A a, B b);
	
	@Override
	public boolean isMutable() {
		return false;
	}
	
	@Override
	public MutableValueModel<N> asMutable() {
		return null;
	}
	
	@Override
	public boolean isConstant() {
		return false;
	}
	
	@Override
	public ImmutableValueModel<N> asImmutable() {
		return this;
	}
	
	@Override
	public ConstantValueModel<N> asConstant() {
		return null;
	}
	
	@Override
	public N get() {
		return this.result.get();
	}

	private void compute(final ValueModel<A> a, final ValueModel<B> b) {
		N newResult = compute(a.get(), b.get());
		result.set(newResult);
	}
}
