/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model;

public class ListPositionModel<E>
	extends PositionModel {
	
	private ListModel<E> model;

	public ListPositionModel(ListModel<E> model) {
		super();
		
		if (model == null) {
			throw new NullPointerException("model");
		}
		
		this.model = model;
	}	
		
	public E getValue() {
		Integer p = get();		
		return (p != null) && isValid(p) ? model.get().get(p.intValue()) : null;
	}


	@Override
	protected int limit() {
		return model.get().size();
	}
	
	
	}
