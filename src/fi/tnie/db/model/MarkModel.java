/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public class MarkModel<S>
	extends BooleanTransformationModel<S> {
	
	private S mark;
	
	public MarkModel(ValueModel<S> source, S mark) {
		super(source);
		this.mark = mark;
	}
	
	protected boolean match(S value, S mark) {
		return (value == mark);
	}

	@Override
	public Boolean transform(S value) {
		boolean m = match(value, this.mark);		
		return Boolean.valueOf(m);
	}
}
