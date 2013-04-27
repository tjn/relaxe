/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.util.List;

import com.appspot.relaxe.env.Implementation;
import com.appspot.relaxe.env.pg.PGImplementation;


public class ImplementationVariable<I extends Implementation<I>> {
		
	private I value;
		
	public ImplementationVariable(I imp, List<DriverVersionVariable<I>> drivers) {
		super();
		this.value = imp;
		
		new PGImplementation();
	}
	
	public I implementation() {
		return value;				
	}
	
	
	
}
