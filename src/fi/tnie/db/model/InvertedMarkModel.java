/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;


/***
 * TODO: Considering the Liskov's substitution principle, it looks highly suspicious to inherit InvertedMarkModel from MarkModel...    
 *  
 * @author Topi Nieminen <topi.nieminen@gmail.com>
 *
 * @param <S>
 */
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
