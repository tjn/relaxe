/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

import java.util.Date;

public class MutableDateModel
	extends DefaultMutableValueModel<Date> {

	public MutableDateModel() {
		super();
	}

	public MutableDateModel(Date value) {
		super(clone(value));
	}	
	
	@Override
	public Date get() {
		Date d = super.get();
		return d == null ? null : clone(d);
	}
	
	@Override
	public void set(Date newValue) {
		Date d = (newValue == null) ? null : clone(newValue);
		super.set(d);
	}

	private static Date clone(Date newValue) {
		return new Date(newValue.getTime());
	}
}
