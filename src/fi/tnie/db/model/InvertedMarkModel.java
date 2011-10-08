/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public class InvertedMarkModel<S>
	extends MarkModel<S> {

	public InvertedMarkModel(ValueModel<S> source, S mark) {
		super(source, mark);
	}
	
	@Override
	protected boolean match(S value, S mark) {
		return (value != mark);
	}
}
