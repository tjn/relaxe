/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public interface BooleanModel
	extends ValueModel<Boolean> {
	
	@Override
	public MutableBooleanModel asMutable();
	
}
