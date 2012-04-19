/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public class EmptyModel
	extends BooleanTransformationModel<String> {

	public EmptyModel(ValueModel<String> source) {
		super(source);
	}
	
	@Override
	public Boolean transform(String source) {
		return (source == null) || source.trim().equals("");
	}
}
