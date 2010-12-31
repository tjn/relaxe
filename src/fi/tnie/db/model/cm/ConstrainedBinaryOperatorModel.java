/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model.cm;

public abstract class ConstrainedBinaryOperatorModel<V, A, B>
	extends AbstractConstrainedValueModel<V> {
	
	private ConstrainedValueModel<A> a; 
	private ConstrainedValueModel<B> b;
	
	public ConstrainedBinaryOperatorModel(final ConstrainedValueModel<A> a, final ConstrainedValueModel<B> b) {
		Constraint c = new Constraint() {				
			@Override
			public void apply(ChangeSet cs, final Proposition p) {
				A av = a.proposed(cs);
				B bv = b.proposed(cs);				
				V newValue = compute(av, bv);	
								
				Proposition np = cs.submit(ConstrainedBinaryOperatorModel.this, newValue, p);
																				
				if (np.isRejected()) {
					p.reject();
				}
			}
		};
		
		a.addConstraint(c);		
		b.addConstraint(c);
				
		this.a = a;
		this.b = b;
	}	
		
	public abstract V compute(A a, B b);
	
	public V get() {
		return compute(this.a.get(), this.b.get());
	}
	
}