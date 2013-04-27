/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model;

public class MutableStringModel
	extends DefaultMutableValueModel<String>
	implements StringModel {

	public MutableStringModel() {
		super();
	}

	public MutableStringModel(String value) {
		super(value);	
	}		
}
