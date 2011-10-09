/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public abstract class BinaryRelationModel<N>
	extends ImmutableBooleanModel {
	
	private MutableBooleanModel result = new MutableBooleanModel();

	public BinaryRelationModel(final ValueModel<N> a, final ValueModel<N> b) {		
		ChangeListener<N> cl = new ChangeListener<N>() {
			@Override
			public void changed(N from, N to) {				
				Boolean newResult = apply(a.get(), b.get());
				result.set(newResult);
			}
		};
		
		if (!a.isConstant()) {
			a.addChangeHandler(cl);
		}
		
		if (!b.isConstant()) {
			b.addChangeHandler(cl);
		}
		
		this.result.set(apply(a.get(), b.get()));
	}

	protected abstract Boolean apply(N a, N b);

	
	@Override
	public Registration addChangeHandler(ChangeListener<Boolean> ch) {
		return this.result.addChangeHandler(ch);
	}
	
	@Override
	public Boolean get() {
		return result.get();
	}

	public static class Gt<V extends Comparable<V>>
		extends BinaryRelationModel<V>
	{
		public Gt(ValueModel<V> a, ValueModel<V> b) {
			super(a, b);
		}
		
		@Override
		protected Boolean apply(V a, V b) {
			if (a == null || b == null) {
				return null;
			}
			
			int result = a.compareTo(b);
			return Boolean.valueOf(result > 0);
		}
	}
	
	public class Eq<V>
		extends BinaryRelationModel<V>
	{
		public Eq(ValueModel<V> a, ValueModel<V> b) {
			super(a, b);
		}
		
		@Override
		protected Boolean apply(V a, V b) {		
			return Boolean.valueOf(a.equals(b));
		}
	}
	
	public static class Lt<V extends Comparable<V>>
		extends BinaryRelationModel<V>
	{
		public Lt(ValueModel<V> a, ValueModel<V> b) {
			super(a, b);
		}
		
		@Override
		protected Boolean apply(V a, V b) {
			if (a == null || b == null) {
				return null;
			}
			
			int result = a.compareTo(b);
			return Boolean.valueOf(result < 0);
		}
	}
	
}
