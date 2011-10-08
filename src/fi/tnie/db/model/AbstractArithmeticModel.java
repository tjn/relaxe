/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public abstract class AbstractArithmeticModel<N>
	implements ImmutableValueModel<N> {

	private MutableValueModel<N> result = new DefaultMutableValueModel<N>(null);

	public AbstractArithmeticModel(final ValueModel<N> a, final ValueModel<N> b) {		
		ChangeListener<N> cl = new ChangeListener<N>() {
			@Override
			public void changed(N from, N to) {	
				N newResult = compute(a.get(), b.get());
				result.set(newResult);
			}
		};
		
		if (!a.isConstant()) {
			a.addChangeHandler(cl);
		}
		
		if (!b.isConstant()) {
			b.addChangeHandler(cl);
		}
		
		this.result.set(compute(a.get(), b.get()));
	}
	
	@Override
	public Registration addChangeHandler(ChangeListener<N> ch) {
		return this.result.addChangeHandler(ch);
	}

	protected abstract N compute(N a, N b);
	
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
}
